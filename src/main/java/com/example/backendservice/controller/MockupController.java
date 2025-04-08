package com.example.backendservice.controller;

import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.request.UserPasswordRequest;
import com.example.backendservice.controller.request.UserUpdateRequest;
import com.example.backendservice.controller.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mockup/user")
@Tag(name = "Mockup User controller")
public class MockupController {
    @Operation(summary = "Get user list", description = "Api retrieve user from db")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(1l);
        userResponse1.setFirstName("John");
        userResponse1.setLastName("Doe");
        userResponse1.setUsername("johndoe");
        userResponse1.setBirthday("1990-01-01");
        userResponse1.setEmail("nhq@gmail.com");
        userResponse1.setPhone("123456789");
//        userResponse1.setGender("male");
        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2l);
        userResponse2.setFirstName("Huu");
        userResponse2.setLastName("Quan");
        userResponse2.setUsername("huuquan");
        userResponse2.setBirthday("2004-12-24");
        userResponse2.setEmail("nhq2@gmail.com");
        userResponse2.setPhone("987654321");
//        userResponse2.setGender("male");
        List<UserResponse> userResponses = List.of(userResponse1, userResponse2);
        Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", HttpStatus.OK.value());
            result.put("message", "success");
            result.put("data", userResponses);

        return result;
    }

    @Operation(summary = "Get user detail", description = "Api retrieve user detail from db")
    @GetMapping("/{userId}")
    public Map<String, Object> getUSerDetail(@PathVariable Long userId){
        UserResponse userDetail = new UserResponse();
        userDetail.setId(userId);
        userDetail.setFirstName("Huu");
        userDetail.setLastName("Quan");
        userDetail.setUsername("huuquan");
        userDetail.setBirthday("2004-12-24");
        userDetail.setEmail("nhq2@gmail.com");
        userDetail.setPhone("987654321");
//        userDetail.setGender("male");
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "get user detail");
        result.put("data", userDetail);
        return result;
    }

    @Operation(summary = "Create user", description = "Api create user")
    @PostMapping("/add")
    public Map<String, Object> createUser(@RequestBody UserCreationRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "create user");
        result.put("data", 3);
        return result;
    }

    @Operation(summary = "Update user", description = "Api update user")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody UserUpdateRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "update user");
        result.put("data", "");
        return result;
    }

    @Operation(summary = "Change password", description = "Api change password")
    @PatchMapping("/change-pwd")
    public Map<String, Object> changePassword(@RequestBody UserPasswordRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "change pwd");
        result.put("data", "");
        return result;
    }

    @Operation(summary = "Delete user", description = "Api inactivate user from database")
    @DeleteMapping("/delete/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Long userId) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "delete user");
        result.put("data", userId);
        return result;
    }
}
