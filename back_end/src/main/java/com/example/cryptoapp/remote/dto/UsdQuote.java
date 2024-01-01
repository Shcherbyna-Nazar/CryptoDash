package com.example.cryptoapp.remote.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsdQuote {
    private double price;
    private long volume_24h;
    private double market_cap;
    private long circulating_supply;
    private long total_supply;
    private String timestamp;
}

