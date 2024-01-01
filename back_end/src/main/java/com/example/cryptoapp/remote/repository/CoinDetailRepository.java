package com.example.cryptoapp.remote.repository;

import com.example.cryptoapp.remote.model.CoinDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinDetailRepository extends JpaRepository<CoinDetail, String> {
}
