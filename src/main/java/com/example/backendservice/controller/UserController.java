package com.example.backendservice.controller;

import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.request.UserPasswordRequest;
import com.example.backendservice.controller.request.UserUpdateRequest;
import com.example.backendservice.controller.response.UserPageResponse;
import com.example.backendservice.controller.response.UserResponse;
import com.example.backendservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) String sort,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        log.info("get user list: {}, {}, {}, {}", keyword, sort, page, size);
        Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", HttpStatus.OK.value());
            result.put("message", "success");
            result.put("data", userService.findAll(keyword, sort, page, size));

        return result;
    }

    @Operation(summary = "Get user detail", description = "Api retrieve user detail from db")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user')")
    public Map<String, Object> getUSerDetail(@PathVariable @Min(value = 1, message = "userId must be equals or greater than 1") Long userId){
        log.info("get user detail: {}", userId);
//        userService.findById(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "get user detail");
        result.put("data", userService.findById(userId));
        return result;
    }

    @Operation(summary = "Create user", description = "Api create user")
    @PostMapping("/add")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreationRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "create user");
        result.put("data", userService.save(request));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user", description = "Api update user")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        log.info("update user: {}", request);
        userService.update(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "update user");
        result.put("data", "");
        return result;
    }

    @Operation(summary = "Change password", description = "Api change password")
    @PatchMapping("/change-pwd")
    public Map<String, Object> changePassword(@RequestBody UserPasswordRequest request) {
        log.info("change password: {}", request);

        userService.updatePassword(request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "change pwd");
        result.put("data", "");
        return result;
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
    public Map<String, Object> deleteUser(@PathVariable @Min(value = 1, message = "userId must be equals or greater than 1") Long userId) {
        log.info("delete user: {}", userId);
        userService.delete(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "delete user");
        result.put("data", userId);
        return result;
    }
}
