package com.example.lab7_20206466.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idusuario;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @ManyToOne
    @JoinColumn(name = "roleId")
    private Rol rol;
    @Column(nullable = false)
    private String nombre;


}
