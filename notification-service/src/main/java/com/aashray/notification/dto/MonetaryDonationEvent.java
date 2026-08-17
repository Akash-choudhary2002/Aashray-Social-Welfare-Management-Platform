package com.aashray.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/** Mirrors monetary-donation-service's MonetaryDonationEvent payload. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonetaryDonationEvent implements Serializable {
    private Long donationId;
    private BigDecimal amount;
    private String referenceNumber;
    private Long donorId;
    private String donorName;
    private String donorEmail;
}
