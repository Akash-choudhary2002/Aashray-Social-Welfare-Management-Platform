package com.aashray.auth.service;

import com.aashray.auth.dto.UpdateProfileRequest;
import com.aashray.auth.dto.UserProfileResponse;
import com.aashray.auth.entity.Role;
import com.aashray.auth.entity.User;
import com.aashray.auth.exception.UserNotFoundException;
import com.aashray.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getProfile(Long userId) {
        return toProfileResponse(findUserOrThrow(userId));
    }

    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findUserOrThrow(userId);

        if (request.fullName() != null && !request.fullName().isBlank()) {
            user.setFullName(request.fullName());
        }
        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }
        if (request.address() != null) {
            user.setAddress(request.address());
        }
        if (request.city() != null) {
            user.setCity(request.city());
        }

        return toProfileResponse(userRepository.save(user));
    }

    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toProfileResponse)
                .toList();
    }

    public List<UserProfileResponse> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::toProfileResponse)
                .toList();
    }

    @Transactional
    public void setUserEnabled(Long userId, boolean enabled) {
        User user = findUserOrThrow(userId);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    /**
     * Backs the Admin Dashboard "Total Users / Donors / NGOs..." widgets.
     */
    public Map<String, Long> getUserCountsByRole() {
        return Map.of(
                "totalUsers", userRepository.count(),
                "totalDonors", userRepository.countByRole(Role.DONOR),
                "totalNgos", userRepository.countByRole(Role.NGO),
                "totalVolunteers", userRepository.countByRole(Role.VOLUNTEER),
                "totalEducators", userRepository.countByRole(Role.EDUCATOR),
                "totalBeneficiaries", userRepository.countByRole(Role.BENEFICIARY)
        );
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private UserProfileResponse toProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .city(user.getCity())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
