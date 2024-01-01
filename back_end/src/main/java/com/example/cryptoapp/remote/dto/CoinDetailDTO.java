package com.example.cryptoapp.remote.dto;

import com.example.cryptoapp.remote.model.CoinDetail;
import com.example.cryptoapp.remote.model.CoinLink;
import com.example.cryptoapp.remote.model.MarketData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinDetailDTO {

    private String id;
    private String symbol;
    private String name;

    @JsonProperty("asset_platform_id")
    private String assetPlatformId;

    private Map<String, String> platforms;
    @JsonProperty("hashing_algorithm")
    private String hashingAlgorithm;
    private List<String> categories;
    private Map<String, String> description;
    private Map<String, Object> links;
    private Map<String, String> image;

    @JsonProperty("country_origin")
    private String countryOrigin;

    @JsonProperty("genesis_date")
    private String genesisDate;

    @JsonProperty("sentiment_votes_up_percentage")
    private Double sentimentVotesUpPercentage;

    @JsonProperty("sentiment_votes_down_percentage")
    private Double sentimentVotesDownPercentage;

    @JsonProperty("market_cap_rank")
    private Integer marketCapRank;

    @JsonProperty("coingecko_rank")
    private Integer coingeckoRank;

    @JsonProperty("coingecko_score")
    private Double coingeckoScore;

    @JsonProperty("developer_score")
    private Double developerScore;

    @JsonProperty("community_score")
    private Double communityScore;

    @JsonProperty("liquidity_score")
    private Double liquidityScore;

    @JsonProperty("public_interest_score")
    private Double publicInterestScore;

    @JsonProperty("market_data")
    private MarketData marketData;

    public static CoinDetail toEntity(CoinDetailDTO coinDetailDTO) {
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setId(coinDetailDTO.getId());
        coinDetail.setSymbol(coinDetailDTO.getSymbol());
        coinDetail.setName(coinDetailDTO.getName());
        coinDetail.setAssetPlatformId(coinDetailDTO.getAssetPlatformId());
        coinDetail.setPlatforms(coinDetailDTO.getPlatforms());
        coinDetail.setHashingAlgorithm(coinDetailDTO.getHashingAlgorithm());
        coinDetail.setCategories(coinDetailDTO.getCategories());
        coinDetail.setDescription(coinDetailDTO.getDescription());
        List<CoinLink> coinLinks = new ArrayList<>();
        if (coinDetailDTO.getLinks() != null) {
            for (Map.Entry<String, Object> entry : coinDetailDTO.getLinks().entrySet()) {
                String linkType = entry.getKey();
                Object linkValue = entry.getValue();

                if (linkValue instanceof List) {
                    ((List<String>) linkValue).forEach(url -> {
                        if (!url.isEmpty()) {
                            CoinLink coinLink = new CoinLink();
                            coinLink.setCoinDetail(coinDetail);
                            coinLink.setLinkType(linkType);
                            coinLink.setLinkValue(url);
                            coinLinks.add(coinLink);
                        }
                    });
                } else if (linkValue instanceof String) {
                    CoinLink coinLink = new CoinLink();
                    coinLink.setCoinDetail(coinDetail);
                    coinLink.setLinkType(linkType);
                    coinLink.setLinkValue((String) linkValue);
                    coinLinks.add(coinLink);
                }
            }
        }
        coinDetail.setLinks(coinLinks);
        coinDetail.setImage(coinDetailDTO.getImage());
        coinDetail.setCountryOrigin(coinDetailDTO.getCountryOrigin());
        coinDetail.setGenesisDate(coinDetailDTO.getGenesisDate());
        coinDetail.setSentimentVotesUpPercentage(coinDetailDTO.getSentimentVotesUpPercentage());
        coinDetail.setSentimentVotesDownPercentage(coinDetailDTO.getSentimentVotesDownPercentage());
        coinDetail.setMarketCapRank(coinDetailDTO.getMarketCapRank());
        coinDetail.setCoingeckoRank(coinDetailDTO.getCoingeckoRank());
        coinDetail.setCoingeckoScore(coinDetailDTO.getCoingeckoScore());
        coinDetail.setDeveloperScore(coinDetailDTO.getDeveloperScore());
        coinDetail.setCommunityScore(coinDetailDTO.getCommunityScore());
        coinDetail.setLiquidityScore(coinDetailDTO.getLiquidityScore());
        coinDetail.setPublicInterestScore(coinDetailDTO.getPublicInterestScore());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String marketDataJson = objectMapper.writeValueAsString(coinDetailDTO.getMarketData());
            coinDetail.setMarketData(marketDataJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // handle the exception appropriately
        }
        return coinDetail;

    }


}
