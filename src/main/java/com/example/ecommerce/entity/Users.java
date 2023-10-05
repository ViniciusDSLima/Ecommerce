package com.example.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String role;

    private String mobile;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany
    @CollectionTable(name = "payment_information", joinColumns = @JoinColumn(name = "user_id"))
    private List<PaymentInformation> paymentInformation = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    private LocalDateTime createdAt;


}
