package com.example.cryptoapp.remote.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_last_update")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointLastUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "endpoint_name")
    private String endpointName;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
