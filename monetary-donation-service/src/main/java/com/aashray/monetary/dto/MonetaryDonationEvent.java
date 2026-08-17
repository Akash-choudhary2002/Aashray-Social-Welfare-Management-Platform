package com.aashray.monetary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonetaryDonationEvent implements Serializable {
    private Long donationId;
    private BigDecimal amount;
    private String referenceNumber;
    private Long donorId;
    private String donorName;
    private String donorEmail;
}
