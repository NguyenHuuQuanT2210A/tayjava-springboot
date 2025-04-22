package com.example.backendservice.controller;

import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.request.UserPasswordRequest;
import com.example.backendservice.controller.request.UserUpdateRequest;
import com.example.backendservice.controller.response.ApiResponse;
import com.example.backendservice.controller.response.UserPageResponse;
import com.example.backendservice.controller.response.UserResponse;
import com.example.backendservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "User controller")
@RequiredArgsConstructor
@Slf4j(topic = "USERCONTROLLER")
@Validated
public class UserController {
    private final UserService userService;


    @Operation(summary = "Get user list", description = "Api retrieve user from db")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('sysadmin', 'admin')")
    public ApiResponse getList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String sort,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {
        log.info("get user list: {}, {}, {}, {}", keyword, sort, page, size);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("user list")
                .data(userService.findAll(keyword, sort, page, size))
                .build();
    }

    @Operation(summary = "Get user detail", description = "Api retrieve user detail from db")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user')")
    public ApiResponse getUSerDetail(@PathVariable @Min(value = 1, message = "userId must be equals or greater than 1") Long userId){
        log.info("get user detail: {}", userId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("get user detail")
                .data(userService.findById(userId))
                .build();
    }

    @Operation(summary = "Get user by username", description = "Api get user by username")
    @GetMapping("/search/username/{username}")
    public ApiResponse getUserByUsername(@PathVariable @NotBlank String username){
        log.info("get user by username: {}", username);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("get user by username")
                .data(userService.findByUsername(username))
                .build();
    }

    @Operation(summary = "Create user", description = "Api create user")
    @PostMapping("/add")
    public ApiResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("create user")
                .data(userService.save(request))
                .build();
    }

    @Operation(summary = "Update user", description = "Api update user")
    @PutMapping("/update")
    public ApiResponse updateUser(@RequestBody @Valid UserUpdateRequest request) {
        log.info("update user: {}", request);
        userService.update(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("update user")
                .data("")
                .build();
    }

    @Operation(summary = "Change password", description = "Api change password")
    @PatchMapping("/change-pwd")
    public ApiResponse changePassword(@RequestBody UserPasswordRequest request) {
        log.info("change password: {}", request);
        userService.updatePassword(request);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("change pwd")
                .data("")
                .build();
    }

    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("confirm email: {}", secretCode);
        try {
            // Call the service to confirm the email
        } catch (Exception e) {
            log.error("Error confirming email: {}", e.getMessage());
            throw new RuntimeException("Error confirming email");
        } finally {
            response.sendRedirect("http://localhost:3000/home");
        }
    }

    @Operation(summary = "Delete user", description = "Api inactivate user from database")
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('admin')")
    public ApiResponse deleteUser(@PathVariable @Min(value = 1, message = "userId must be equals or greater than 1") Long userId) {
        log.info("delete user: {}", userId);
        userService.delete(userId);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("delete user")
                .data(userId)
                .build();
    }
}
