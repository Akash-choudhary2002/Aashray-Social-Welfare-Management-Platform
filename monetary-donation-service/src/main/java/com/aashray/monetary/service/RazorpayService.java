package com.aashray.monetary.service;

import com.aashray.monetary.config.RazorpayConfig;
import com.aashray.monetary.dto.RazorpayOrder;
import com.aashray.monetary.exception.PaymentGatewayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Talks to Razorpay's official REST API directly:
 *   - POST /v1/orders    -> creates the order the donor pays against
 *   - GET  /v1/payments/{id} -> enriches a verified donation with the
 *                               real payment method Razorpay recorded
 *
 * Both calls use HTTP Basic Auth with the key id/secret, exactly as
 * Razorpay's server-side API docs specify.
 *
 * Signature verification is done locally per Razorpay's documented
 * algorithm (HMAC-SHA256 of "order_id|payment_id" using the key secret)
 * rather than trusting anything the browser reports about payment
 * outcome — this is the one piece of the whole flow that is not allowed
 * to trust the frontend.
 */
@Service
public class RazorpayService {

    private static final Logger log = LoggerFactory.getLogger(RazorpayService.class);

    private static final String ORDERS_URL = "https://api.razorpay.com/v1/orders";
    private static final String PAYMENTS_URL = "https://api.razorpay.com/v1/payments";

    private final RestTemplate restTemplate;
    private final RazorpayConfig config;

    public RazorpayService(RestTemplate razorpayRestTemplate, RazorpayConfig config) {
        this.restTemplate = razorpayRestTemplate;
        this.config = config;
    }

    /**
     * Creates a Razorpay order for the given amount. Throws
     * PaymentGatewayException (never a raw RestClientException) on any
     * failure so the caller/controller never has to know this is an
     * HTTP call under the hood.
     */
    public RazorpayOrder createOrder(BigDecimal amount, String currency, String receipt) {
        long amountInSubUnits = amount
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(config.getKeyId(), config.getKeySecret());

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amountInSubUnits);
        body.put("currency", currency);
        body.put("receipt", receipt);
        body.put("payment_capture", 1);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(ORDERS_URL, entity, Map.class);
            Map<?, ?> responseBody = response.getBody();

            if (responseBody == null || responseBody.get("id") == null) {
                log.error("Razorpay order creation returned no order id. Response: {}", responseBody);
                throw new PaymentGatewayException("Payment gateway did not return an order id. Please try again.");
            }

            return new RazorpayOrder((String) responseBody.get("id"), amountInSubUnits, currency);
        } catch (RestClientException ex) {
            log.error("Razorpay order creation failed: {}", ex.getMessage());
            throw new PaymentGatewayException("Unable to reach the payment gateway right now. Please try again.");
        }
    }

    /**
     * Fetches payment details from Razorpay to record the *actual*
     * method used (upi/card/netbanking/wallet) rather than trusting
     * anything the donor selected on our own form. Non-fatal on
     * failure — the donation is already verified via signature by the
     * time this runs, so a failed enrichment call must never block a
     * successful donation.
     */
    public Map<?, ?> fetchPayment(String paymentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(config.getKeyId(), config.getKeySecret());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    PAYMENTS_URL + "/" + paymentId, HttpMethod.GET, entity, Map.class);
            Map<?, ?> body = response.getBody();
            return body != null ? body : Map.of();
        } catch (RestClientException ex) {
            log.warn("Could not fetch Razorpay payment details for paymentId={}: {}", paymentId, ex.getMessage());
            return Map.of();
        }
    }

    /**
     * Verifies that (orderId, paymentId, signature) is an authentic,
     * untampered triple from Razorpay — the payload Razorpay signs is
     * exactly "{order_id}|{payment_id}", HMAC-SHA256'd with the key
     * secret, hex-encoded. Uses a constant-time comparison to avoid
     * leaking signature bytes via timing.
     */
    public boolean verifySignature(String orderId, String paymentId, String signature) {
        if (orderId == null || paymentId == null || signature == null) {
            return false;
        }
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(config.getKeySecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String computed = HexFormat.of().formatHex(hash);
            return MessageDigest.isEqual(
                    computed.getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            log.error("Signature verification failed unexpectedly: {}", e.getMessage());
            return false;
        }
    }
}
