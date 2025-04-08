package com.example.backendservice.repository;

import com.example.backendservice.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.status = 'ACTIVE' " +
            "AND (u.firstName like ?1 " +
            "OR u.email like ?1 " +
            "OR u.lastName like ?1 " +
            "OR u.username like ?1 " +
            "OR u.phone like ?1)")
    Page<UserEntity> searchByKeyword(String keyword, Pageable pageable);
}
