package com.example.backendservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user_has_role")
public class UserHasRole extends AbstractEntity<Integer> implements Serializable {
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
