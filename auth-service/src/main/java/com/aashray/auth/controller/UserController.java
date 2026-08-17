package com.aashray.auth.controller;

import com.aashray.auth.dto.ApiResponse;
import com.aashray.auth.dto.UpdateProfileRequest;
import com.aashray.auth.dto.UserProfileResponse;
import com.aashray.auth.security.CustomUserDetails;
import com.aashray.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "User Profile", description = "Authenticated user's own profile")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get the logged-in user's profile")
    public ApiResponse<UserProfileResponse> getProfile(@AuthenticationPrincipal CustomUserDetails principal) {
        return ApiResponse.success(userService.getProfile(principal.getUser().getId()));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update the logged-in user's profile")
    public ApiResponse<UserProfileResponse> updateProfile(@AuthenticationPrincipal CustomUserDetails principal,
                                                            @Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.success("Profile updated", userService.updateProfile(principal.getUser().getId(), request));
    }
}
