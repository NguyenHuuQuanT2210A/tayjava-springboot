package com.example.backendservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_permission")
public class Permission extends AbstractEntity<Integer> implements Serializable {
    private String name;
    private String description;

    @OneToMany(mappedBy = "permission")
    private Set<RoleHasPermission> roles = new HashSet<>();
}
