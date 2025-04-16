package com.example.backendservice.service.impl;

import com.example.backendservice.common.UserStatus;
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
import com.example.backendservice.service.EmailService;
import com.example.backendservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
     private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {
        log.info("Find all start");
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()){
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")){
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }

        //xu ly th fe muon bat dau vois page = 1
        int pageNo = 0;
        if (page > 0){
            pageNo = page - 1;
        }

        // paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));

        Page<UserEntity> entityPage = null;

        if (StringUtils.hasLength(keyword)){
            keyword = "%" + keyword.toLowerCase() + "%";
            entityPage = userRepository.searchByKeyword(keyword, pageable);
        } else {
            entityPage = userRepository.findAll(pageable);
        }

        return getUserPageResponse(entityPage);
    }



    @Override
    public UserResponse findById(Long id) {
        UserEntity user = getUserEntity(id);
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
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

        UserEntity userByEmail = userRepository.findByEmail(req.getEmail());
        if (userByEmail != null) {
            throw new RuntimeException("Email already exists");
        }

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

        //send email confirm
        try {
            emailService.emailVerification(req.getEmail(), req.getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest req) {
        log.info("Update user {}", req);
        UserEntity user = getUserEntity(req.getId());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setUsername(req.getUsername());
        user.setGender(req.getGender());
        user.setBirthday(req.getBirthday());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        userRepository.save(user);
        log.info("User {} updated successfully", user.getId());

        // Update addresses if any
        List<AddressEntity> addresses = new ArrayList<>();
        req.getAddresses().forEach(address -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(user.getId(), address.getAddressType());
            if (addressEntity == null) {
                addressEntity = new AddressEntity();
            }
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
    }

    @Override
    public void updatePassword(UserPasswordRequest req) {
        log.info("Update password {}", req);
        UserEntity user = getUserEntity(req.getId());
        if (req.getPassword().equals(req.getConfirmPassword())){
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        userRepository.save(user);
        log.info("User {} updated password successfully", user.getId());
    }

    @Override
    public void delete(Long id) {
        log.info("Delete user {}", id);
        UserEntity user = getUserEntity(id);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        log.info("User {} deleted successfully", id);
    }

    /**
     * Get user entity by id
     * @param id
     * @return
     */
    private UserEntity getUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Convert entity to response
     * return page no, page size, list data
     * @param userEntities
     * @return
     */
    private static UserPageResponse getUserPageResponse(Page<UserEntity> userEntities) {
        log.info("converting entity to response");
        List<UserResponse> userResponses = userEntities.stream().map(userEntity ->
                UserResponse.builder()
                        .id(userEntity.getId())
                        .firstName(userEntity.getFirstName())
                        .lastName(userEntity.getLastName())
                        .username(userEntity.getUsername())
                        .gender(userEntity.getGender())
                        .birthday(userEntity.getBirthday())
                        .phone(userEntity.getPhone())
                        .email(userEntity.getEmail())
                        .build()
        ).toList();
        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setPageNumber(userEntities.getNumber());
        userPageResponse.setPageSize(userEntities.getSize());
        userPageResponse.setTotalElements(userEntities.getTotalElements());
        userPageResponse.setTotalPages(userEntities.getTotalPages());
        userPageResponse.setUsers(userResponses);
        return userPageResponse;
    }
}
