package com.aashray.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationStatusEvent implements Serializable {
    private Long donationId;
    private String foodName;
    private Long donorId;
    private String donorName;
    private Long ngoId;
    private String ngoName;
    private String status; // ACCEPTED | PICKED_UP | DELIVERED
}
