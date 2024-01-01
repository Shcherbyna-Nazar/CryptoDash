package com.example.cryptoapp.remote.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {
    private double price;
    @JsonProperty("volume_24h")
    private double volume24h;
    @JsonProperty("volume_change_24h")
    private double volumeChange24h;
    @JsonProperty("percent_change_1h")
    private double percentChange1h;
    @JsonProperty("percent_change_24h")
    private double percentChange24h;
    @JsonProperty("percent_change_7d")
    private double percentChange7d;
    @JsonProperty("market_cap")
    private double marketCap;
    @JsonProperty("market_cap_dominance")
    private double marketCapDominance;
    @JsonProperty("fully_diluted_market_cap")
    private double fullyDilutedMarketCap;
    @JsonProperty("last_updated")
    private String lastUpdated;

}
