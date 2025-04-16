package com.example.backendservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private T id;

    @Column(name = "created_at")
    @CreationTimestamp ///tu dong insert thoi gian tao record
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp  ///tu dong cap nhat thoi gian update record
    private Date updatedAt;
}
