package com.example.cryptoapp.remote.controller;

import com.example.cryptoapp.remote.dto.CoinDetailDTO;
import com.example.cryptoapp.remote.model.CoinDetail;
import com.example.cryptoapp.remote.dto.CryptoQuoteResponse;
import com.example.cryptoapp.remote.model.Coin;
import com.example.cryptoapp.remote.service.CoinMarketCapService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CryptocurrencyController {
    private final CoinMarketCapService coinMarketCapService;

    @GetMapping("/api/v1/crypto/all")
    public ResponseEntity<Page<Coin>> getAllCoins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<Coin> coinsPage = coinMarketCapService.getAllCoins(page, size, sortBy);
        return ResponseEntity.ok(coinsPage);
    }

    @GetMapping("/api/v1/crypto/details/{coinId}")
    public ResponseEntity<CoinDetail> getCoinDetail(@PathVariable String coinId) {
        CoinDetail coinDetail = coinMarketCapService.getCoinDetail(coinId);
        if (coinDetail != null) {
            return ResponseEntity.ok(coinDetail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
