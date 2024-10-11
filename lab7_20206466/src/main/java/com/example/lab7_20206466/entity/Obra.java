package com.example.lab7_20206466.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity (name = "obras")
@Getter
@Setter
public class Obra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Integer id;
    private String title;
    private String description;
    private int duration;
    private Date releaseDate;

}
