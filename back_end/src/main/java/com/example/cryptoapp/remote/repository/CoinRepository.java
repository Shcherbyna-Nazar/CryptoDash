package com.example.cryptoapp.remote.repository;

import com.example.cryptoapp.remote.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {
    Optional<Coin> findTopByOrderByLastUpdatedDesc();
}
