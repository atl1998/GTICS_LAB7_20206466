package com.example.lab7_20206466.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity (name = "roles")
@Getter
@Setter
public class Rol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Integer id;

    @Column (name="name")
    private String rolname;

}
