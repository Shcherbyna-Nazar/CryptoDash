package com.example.cryptoapp.remote.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coin_links")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoinLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coin_id", nullable = false)
    @JsonBackReference
    private CoinDetail coinDetail;

    private String linkType;

    private String linkValue; // This holds a single URL or a text value

    // Constructors, getters, and setters
}
