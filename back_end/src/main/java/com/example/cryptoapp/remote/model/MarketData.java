package com.example.cryptoapp.remote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarketData {
    @JsonProperty("current_price")
    private Map<String, Double> currentPrice;
    private Map<String, Double> ath;
    private Map<String, Double> atl;
    @JsonProperty("ath_change_percentage")
    private Map<String, Double> athChangePercentage;
    @JsonProperty("ath_date")
    private Map<String, String> athDate;
    @JsonProperty("atl_date")
    private Map<String, String> atlDate;
    @JsonProperty("market_cap")
    private Map<String, Double> marketCap;
    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;
    @JsonProperty("fully_diluted_valuation")
    private Map<String, Double> fullyDilutedValuation;
    @JsonProperty("market_cap_fdv_ratio")
    private Double marketCapFdvRatio;
    @JsonProperty("total_volume")
    private Map<String, Double> totalVolume;
    @JsonProperty("high_24h")
    private Map<String, Double> high24h;
    @JsonProperty("low_24h")
    private Map<String, Double> low24h;
    @JsonProperty("price_change_24h")
    private Double priceChange24h;
    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;
    @JsonProperty("price_change_percentage_7d")
    private Double priceChangePercentage7d;
    @JsonProperty("price_change_percentage_14d")
    private Double priceChangePercentage14d;
    @JsonProperty("price_change_percentage_30d")
    private Double priceChangePercentage30d;
    @JsonProperty("price_change_percentage_60d")
    private Double priceChangePercentage60d;
    @JsonProperty("price_change_percentage_200d")
    private Double priceChangePercentage200d;
    @JsonProperty("price_change_percentage_1y")
    private Double priceChangePercentage1y;
    @JsonProperty("market_cap_change_24h_in_currency")
    private Map<String, Double> marketCapChange24hInCurrency;
    @JsonProperty("market_cap_change_percentage_24h_in_currency")
    private Map<String, Double> marketCapChangePercentage24hInCurrency;
    @JsonProperty("total_supply")
    private Long totalSupply;
    @JsonProperty("max_supply")
    private Long maxSupply;
    @JsonProperty("circulating_supply")
    private Long circulatingSupply;
    @JsonProperty("last_updated")
    private String lastUpdated;

    // Getters and setters for each field
}
