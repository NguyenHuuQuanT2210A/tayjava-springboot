package com.example.backendservice.service;

import com.example.backendservice.common.Gender;
import com.example.backendservice.common.UserStatus;
import com.example.backendservice.common.UserType;
import com.example.backendservice.controller.request.AddressRequest;
import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.request.UserPasswordRequest;
import com.example.backendservice.controller.request.UserUpdateRequest;
import com.example.backendservice.controller.response.UserPageResponse;
import com.example.backendservice.controller.response.UserResponse;
import com.example.backendservice.exception.InvalidDataException;
import com.example.backendservice.exception.ResourceNotFoundException;
import com.example.backendservice.model.AddressEntity;
import com.example.backendservice.model.UserEntity;
import com.example.backendservice.repository.AddressRepository;
import com.example.backendservice.repository.UserRepository;
import com.example.backendservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static UserService userService;

    private @Mock UserRepository userRepository;
    private @Mock AddressRepository addressRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @Mock EmailService emailService;


    private static UserEntity huuQuan;
    private static UserEntity johnDoe;

    @BeforeAll
    static void beforeAll() {
        huuQuan = new UserEntity();
        huuQuan.setId(1L);
        huuQuan.setFirstName("Quân");
        huuQuan.setLastName("Huu");
        huuQuan.setUsername("huuquan");
        huuQuan.setGender(Gender.MALE);
        huuQuan.setBirthday(String.valueOf(new Date()));
        huuQuan.setEmail("huuquan@gmail.com");
        huuQuan.setPhone("0123456789");
        huuQuan.setType(UserType.USER);
        huuQuan.setStatus(UserStatus.ACTIVE);
        huuQuan.setPassword("abcd");


        johnDoe = new UserEntity();
        johnDoe.setId(2L);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setUsername("JohnDoe");
        johnDoe.setGender(Gender.FEMALE);
        johnDoe.setBirthday(String.valueOf(new Date()));
        johnDoe.setEmail("johndoe@gmail.com");
        johnDoe.setPhone("0987654321");
        johnDoe.setType(UserType.USER);
        johnDoe.setStatus(UserStatus.INACTIVE);
    }

    @BeforeEach
    void setUp() {
        //khoi tao buoc trien khai la UserService
        userService = new UserServiceImpl(userRepository, addressRepository, passwordEncoder, emailService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetListUsers_Success() {
        //gia lap phuong thuc
        Page<UserEntity> userPage = new PageImpl<>(Arrays.asList(huuQuan, johnDoe));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        //goi phuong thuc can test
        UserPageResponse result = userService.findAll(null, null, 0, 5);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testSearchUser_Success() {
        //gia lap phuong thuc
        Page<UserEntity> userPage = new PageImpl<>(Arrays.asList(huuQuan, johnDoe));
        when(userRepository.searchByKeyword(any(), any(Pageable.class))).thenReturn(userPage);

        //goi phuong thuc can test
        UserPageResponse result = userService.findAll("quan", null, 0, 5);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetListUsers_Empty() {
        //gia lap phuong thuc
        Page<UserEntity> userPage = new PageImpl<>(List.of());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        //goi phuong thuc can test
        UserPageResponse result = userService.findAll(null, null, 0, 5);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testGetUserById_Success() {
        //gia lap phuong thuc
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(huuQuan));

        //goi phuong thuc can test
        UserResponse result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetUserById_Failure() {
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.findById(100L));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testGetUserByUsername_Success() {
        String username = "huuquan";
        //gia lap phuong thuc
        when(userRepository.findByUsername(username)).thenReturn(huuQuan);

        //goi phuong thuc can test
        UserResponse result = userService.findByUsername(username);

        //test ket qua
        assertEquals(username, result.getUsername());
    }

    @Test
    void findByEmail() {
        String email = "huuquan@gmail.com";
        //gia lap phuong thuc
        when(userRepository.findByEmail(email)).thenReturn(huuQuan);

        //goi phuong thuc can test
        UserResponse result = userService.findByEmail(email);

        //test ket qua
        assertEquals(email, result.getEmail());
    }

    @Test
    void save() {
        //gia lap phuong thuc
        when(userRepository.save(any(UserEntity.class))).thenReturn(huuQuan);

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

        //goi phuong thuc can test
        long userId = userService.save(userCreationRequest);

        assertEquals(1L, userId);
    }

    @Test
    void updateUser_Success() {
        Long userID = 2L;

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userID);
        updatedUser.setFirstName("Jame");
        updatedUser.setLastName("Smith");
        updatedUser.setUsername("JameSmith");
        updatedUser.setGender(Gender.FEMALE);
        updatedUser.setBirthday(String.valueOf(new Date()));
        updatedUser.setEmail("jamesmith@gmail.com");
        updatedUser.setPhone("0987654321");
        updatedUser.setType(UserType.USER);
        updatedUser.setStatus(UserStatus.ACTIVE);

        //gia lap phuong thuc
        when(userRepository.findById(userID)).thenReturn(java.util.Optional.of(johnDoe));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        //tao request
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(userID);
        userUpdateRequest.setFirstName("Jame");
        userUpdateRequest.setLastName("Smith");
        userUpdateRequest.setUsername("JameSmith");
        userUpdateRequest.setGender(Gender.FEMALE);
        userUpdateRequest.setBirthday(String.valueOf(new Date()));
        userUpdateRequest.setEmail("jamesmith@gmail.com");
        userUpdateRequest.setPhone("0987654321");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("101");
        addressRequest.setFloor("2");
        addressRequest.setBuilding("A");
        addressRequest.setStreetNumber("123");
        addressRequest.setStreet("Main St");
        addressRequest.setCity("Hanoi");
        addressRequest.setCountry("Vietnam");
        addressRequest.setAddressType(1);

        userUpdateRequest.setAddresses(List.of(addressRequest));

        userService.update(userUpdateRequest);

        UserResponse result = userService.findById(userID);

        assertNotNull(result);
        assertEquals("Jame", result.getFirstName());
        assertEquals("Smith", result.getLastName());
    }

    @Test
    void testUpdatePassword_Success() {
        Long id = 1L;
        String password = "12345";
        String confirmPwd = "12345";

        //gia lap phuong thuc
        when(userRepository.findById(id)).thenReturn(java.util.Optional.of(huuQuan));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        //tao request
        UserPasswordRequest userUpdateRequest = new UserPasswordRequest();
        userUpdateRequest.setId(id);
        userUpdateRequest.setPassword(password);
        userUpdateRequest.setConfirmPassword(confirmPwd);

        //goi phuong thuc can test
        userService.updatePassword(userUpdateRequest);

        UserResponse result = userService.findById(id);
        assertNotNull(result);

        verify(passwordEncoder, times(1)).encode(password);
    }

    @Test
    void testUpdatePassword_Failure() {
        Long id = 1L;
        String password = "123456";
        String confirmPwd = "12345";

        //gia lap phuong thuc
        when(userRepository.findById(id)).thenReturn(java.util.Optional.of(huuQuan));

        //tao request
        UserPasswordRequest userUpdateRequest = new UserPasswordRequest();
        userUpdateRequest.setId(id);
        userUpdateRequest.setPassword(password);
        userUpdateRequest.setConfirmPassword(confirmPwd);


        //goi phuong thuc can test
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            userService.updatePassword(userUpdateRequest);
        });

        assertEquals(exception.getMessage(), "Password and confirm password do not match");

        verify(passwordEncoder, times(0)).encode(password);

        verify(userRepository, times(0)).save(huuQuan);

    }

    @Test
    void testDeleteUser_Success() {
        Long userID = 1L;

        //gia lap phuong thuc
        when(userRepository.findById(userID)).thenReturn(java.util.Optional.of(huuQuan));

        //goi phuong thuc can test
        userService.delete(userID);

        assertEquals(UserStatus.INACTIVE, huuQuan.getStatus());
        verify(userRepository, times(1)).save(huuQuan);
    }
}