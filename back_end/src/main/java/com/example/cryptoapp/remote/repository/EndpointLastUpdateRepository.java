package com.example.cryptoapp.remote.repository;

import com.example.cryptoapp.remote.model.EndpointLastUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndpointLastUpdateRepository extends JpaRepository<EndpointLastUpdate, Long> {
    Optional<EndpointLastUpdate> findByEndpointName(String endpointName);
}
