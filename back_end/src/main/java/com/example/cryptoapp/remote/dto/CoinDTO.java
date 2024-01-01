package com.example.cryptoapp.remote.dto;

import com.example.cryptoapp.remote.model.Coin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinDTO {
    private String id;
    private String symbol;
    private String name;
    private String image;

    @JsonProperty("current_price")
    private Double currentPrice;

    @JsonProperty("market_cap")
    private Long marketCap;

    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;

    @JsonProperty("fully_diluted_valuation")
    private Long fullyDilutedValuation;

    @JsonProperty("total_volume")
    private Long totalVolume;

    @JsonProperty("high_24h")
    private Double high24h;

    @JsonProperty("low_24h")
    private Double low24h;

    @JsonProperty("price_change_24h")
    private Double priceChange24h;

    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;

    @JsonProperty("market_cap_change_24h")
    private Double marketCapChange24h;

    @JsonProperty("market_cap_change_percentage_24h")
    private Double marketCapChangePercentage24h;

    @JsonProperty("circulating_supply")
    private Double circulatingSupply;

    @JsonProperty("total_supply")
    private Double totalSupply;

    @JsonProperty("max_supply")
    private Double maxSupply;

    private Double ath;

    @JsonProperty("ath_change_percentage")
    private Double athChangePercentage;

    @JsonProperty("ath_date")
    private String athDate;

    private Double atl;

    @JsonProperty("atl_change_percentage")
    private Double atlChangePercentage;

    @JsonProperty("atl_date")
    private String atlDate;

    private ROI roi;

    @JsonProperty("last_updated")
    private String lastUpdated;

    public static Coin toEntity(CoinDTO coinDto) {
        Coin coin = new Coin();
        coin.setId(coinDto.getId());
        coin.setSymbol(coinDto.getSymbol());
        coin.setName(coinDto.getName());
        coin.setImage(coinDto.getImage());
        coin.setCurrentPrice(coinDto.getCurrentPrice());
        coin.setMarketCap(coinDto.getMarketCap());
        coin.setMarketCapRank(coinDto.getMarketCapRank());
        coin.setFullyDilutedValuation(coinDto.getFullyDilutedValuation());
        coin.setTotalVolume(coinDto.getTotalVolume());
        coin.setHigh24h(coinDto.getHigh24h());
        coin.setLow24h(coinDto.getLow24h());
        coin.setPriceChange24h(coinDto.getPriceChange24h());
        coin.setPriceChangePercentage24h(coinDto.getPriceChangePercentage24h());
        coin.setMarketCapChange24h(coinDto.getMarketCapChange24h());
        coin.setMarketCapChangePercentage24h(coinDto.getMarketCapChangePercentage24h());
        coin.setCirculatingSupply(coinDto.getCirculatingSupply());
        coin.setTotalSupply(coinDto.getTotalSupply());
        coin.setMaxSupply(coinDto.getMaxSupply());
        coin.setAth(coinDto.getAth());
        coin.setAthChangePercentage(coinDto.getAthChangePercentage());
        coin.setAthDate(coinDto.getAthDate());
        coin.setAtl(coinDto.getAtl());
        coin.setAtlChangePercentage(coinDto.getAtlChangePercentage());
        coin.setAtlDate(coinDto.getAtlDate());
        return coin;
    }
}
