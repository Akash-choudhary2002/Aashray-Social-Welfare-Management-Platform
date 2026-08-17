package com.aashray.notification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/** Mirrors food-donation-service's DonationStatusEvent payload. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DonationStatusEvent implements Serializable {
    private Long donationId;
    private String foodName;
    private Long donorId;
    private String donorName;
    private Long ngoId;
    private String ngoName;
    private String status;
}
