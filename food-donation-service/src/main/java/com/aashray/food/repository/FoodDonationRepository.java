package com.aashray.food.repository;

import com.aashray.food.entity.DonationStatus;
import com.aashray.food.entity.FoodDonation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodDonationRepository extends JpaRepository<FoodDonation, Long> {

    List<FoodDonation> findByDonorIdOrderByCreatedAtDesc(Long donorId);

    List<FoodDonation> findByNgoIdOrderByCreatedAtDesc(Long ngoId);

    List<FoodDonation> findByStatusOrderByCreatedAtDesc(DonationStatus status);

    long countByStatus(DonationStatus status);
}
