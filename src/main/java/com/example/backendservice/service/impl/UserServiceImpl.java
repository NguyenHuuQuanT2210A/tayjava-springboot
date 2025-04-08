package com.example.backendservice.service.impl;

import com.example.backendservice.common.UserStatus;
import com.example.backendservice.common.UserType;
import com.example.backendservice.controller.request.UserCreationRequest;
import com.example.backendservice.controller.request.UserPasswordRequest;
import com.example.backendservice.controller.request.UserUpdateRequest;
import com.example.backendservice.controller.response.UserResponse;
import com.example.backendservice.model.AddressEntity;
import com.example.backendservice.model.UserEntity;
import com.example.backendservice.repository.AddressRepository;
import com.example.backendservice.repository.UserRepository;
import com.example.backendservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
     private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<UserResponse> findAll() {
        return List.of();
    }

    @Override
    public UserResponse findById(Long id) {
        return null;
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UserCreationRequest req) {
        log.info("Save user{} ", req);
        UserEntity user = new UserEntity();
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setUsername(req.getUsername());
        user.setGender(req.getGender());
        user.setBirthday(req.getBirthday());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setType(req.getType());
        user.setStatus(UserStatus.NONE);
        userRepository.save(user);
        log.info("SAVE USER {}", user);

        if (user.getId() != null) {
            // Save addresses if any
            System.out.println(10/0);
            log.info("user id is {}", user.getId());
            List<AddressEntity> addresses = new ArrayList<>();
            if (req.getAddresses() != null) {
                req.getAddresses().forEach(address -> {
                    AddressEntity addressEntity = new AddressEntity();
                    addressEntity.setApartmentNumber(address.getApartmentNumber());
                    addressEntity.setFloor(address.getFloor());
                    addressEntity.setBuilding(address.getBuilding());
                    addressEntity.setStreetNumber(address.getStreetNumber());
                    addressEntity.setStreet(address.getStreet());
                    addressEntity.setCity(address.getCity());
                    addressEntity.setCountry(address.getCountry());
                    addressEntity.setAddressType(address.getAddressType());
                    addressEntity.setUserId(user.getId());
                    addresses.add(addressEntity);
                });
                addressRepository.saveAll(addresses);
                log.info("User {} created successfully", user.getId());
            }
        }
        return user.getId();
    }

    @Override
    public void update(UserUpdateRequest req) {

    }

    @Override
    public void updatePassword(UserPasswordRequest req) {

    }

    @Override
    public void delete(Long id) {

    }
}
