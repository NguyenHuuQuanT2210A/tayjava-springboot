package com.example.backendservice.repository;

import com.example.backendservice.model.AddressEntity;
import com.example.backendservice.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
