package com.example.backendservice.controller;

import com.example.backendservice.common.Gender;
import com.example.backendservice.common.TokenType;
import com.example.backendservice.common.UserStatus;
import com.example.backendservice.common.UserType;
import com.example.backendservice.controller.request.AddressRequest;
import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.response.UserPageResponse;
import com.example.backendservice.controller.response.UserResponse;
import com.example.backendservice.model.UserEntity;
import com.example.backendservice.service.JwtService;
import com.example.backendservice.service.UserService;
import com.example.backendservice.service.UserServiceDetail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockitoBean(types = {UserService.class, UserServiceDetail.class, JwtService.class, UserDetailsService.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceDetail userServiceDetail;

    @Autowired
    private JwtService jwtService;

    private static UserResponse huuQuan;
    private static UserResponse johnDoe;

    @BeforeAll
    static void beforeAll() {
        huuQuan = new UserResponse();
        huuQuan.setId(1L);
        huuQuan.setFirstName("Quân");
        huuQuan.setLastName("Huu");
        huuQuan.setUsername("huuquan");
        huuQuan.setGender(Gender.MALE);
        huuQuan.setBirthday(String.valueOf(new Date()));
        huuQuan.setEmail("huuquan@gmail.com");
        huuQuan.setPhone("0123456789");


        johnDoe = new UserResponse();
        johnDoe.setId(2L);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setUsername("JohnDoe");
        johnDoe.setGender(Gender.FEMALE);
        johnDoe.setBirthday(String.valueOf(new Date()));
        johnDoe.setEmail("johndoe@gmail.com");
        johnDoe.setPhone("0987654321");

    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(authorities = {"sysadmin", "admin"})
    void getListUser() throws Exception {
        List<UserResponse> userList = List.of(huuQuan, johnDoe);

        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setTotalElements(2);
        userPageResponse.setTotalPages(1);
        userPageResponse.setPageNumber(0);
        userPageResponse.setPageSize(20);
        userPageResponse.setUsers(userList);

        when(userService.findAll(null, null, 0, 20)).thenReturn(userPageResponse);

        mockMvc.perform(get("/user/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    @WithMockUser(authorities = {"user"})
    void getUSerDetail() throws Exception {
        when(userService.findById(1L)).thenReturn(huuQuan);

        mockMvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("get user detail"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void createUser() throws Exception {
        String token = "mockToken";
        TokenType tokenType = TokenType.ACCESS_TOKEN;
        String expectedUsername = "john.doe";

        // Giả lập hành vi của extractClaim
        when(jwtService.extractUsername(eq(token), eq(tokenType))).thenReturn(expectedUsername);
        //tao request
        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Quân");
        userCreationRequest.setLastName("Huu");
        userCreationRequest.setUsername("huuquan");
        userCreationRequest.setGender(Gender.MALE);
        userCreationRequest.setBirthday(String.valueOf(new Date()));
        userCreationRequest.setEmail("huuquan@gmail.com");
        userCreationRequest.setPhone("0123456789");
        userCreationRequest.setType(UserType.USER);

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("101");
        addressRequest.setFloor("2");
        addressRequest.setBuilding("A");
        addressRequest.setStreetNumber("123");
        addressRequest.setStreet("Main St");
        addressRequest.setCity("Hanoi");
        addressRequest.setCountry("Vietnam");
        addressRequest.setAddressType(1);

        userCreationRequest.setAddresses(List.of(addressRequest));

        //gia lap phuong thuc
        when(userService.save(any())).thenReturn(1L);

        //goi api
        mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("create user"))
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    void updateUser() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void confirmEmail() {
    }

    @Test
    void deleteUser() {
    }
}