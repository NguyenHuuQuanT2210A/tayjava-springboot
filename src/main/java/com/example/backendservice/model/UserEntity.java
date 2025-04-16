package com.example.backendservice.model;

import com.example.backendservice.common.Gender;
import com.example.backendservice.common.UserStatus;
import com.example.backendservice.common.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_user")
public class UserEntity extends AbstractEntity<Long> implements UserDetails, Serializable {

    @Column(name = "first_name", length = 255)
    private String firstName;
    @Column(name = "last_name", length = 255)
    private String lastName;
    @Column(name = "username",unique = true, nullable = false, length = 255)
    private String username;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "gender", length = 255)
    private Gender gender;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth", length = 255)
    private String birthday;
    @Column(name = "password", length = 255)
    private String password;
    @Column(name = "email", length = 255)
    private String email;
    @Column(name = "phone", length = 15)
    private String phone;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", length = 255)
    private UserType type;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", length = 255)
    private UserStatus status;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserHasRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<GroupHasUser> groups = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //b1: get all roles of user
        List<Role> list = roles.stream().map(UserHasRole::getRole).toList();
        //b2: get role names
        List<String> roleNames = list.stream().map(Role::getName).toList();
        //b3: Add role name to authority
        return roleNames.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(this.status);
    }
}
