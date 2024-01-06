package com.example.cryptoapp.remote.repository;

import com.example.cryptoapp.remote.model.Coin;
import com.example.cryptoapp.remote.model.CoinDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinDetailRepository extends JpaRepository<CoinDetail, String> {
}
