package com.example.cryptoapp.remote.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "coin_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoinDetail{

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "asset_platform_id")
    private String assetPlatformId;

    @ElementCollection
    @CollectionTable(name = "coin_platforms", joinColumns = @JoinColumn(name = "coin_id"))
    @MapKeyColumn(name = "platform_name")
    @Column(name = "platform_value")
    private Map<String, String> platforms;

    @Column(name = "hashing_algorithm")
    private String hashingAlgorithm;

    @ElementCollection
    @CollectionTable(name = "coin_categories", joinColumns = @JoinColumn(name = "coin_id"))
    @Column(name = "category")
    private List<String> categories;

    @ElementCollection
    @CollectionTable(name = "coin_descriptions", joinColumns = @JoinColumn(name = "coin_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "description", columnDefinition = "text")
    private Map<String, String> description;

    @OneToMany(mappedBy = "coinDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CoinLink> links;

    @ElementCollection
    @CollectionTable(name = "coin_images", joinColumns = @JoinColumn(name = "coin_id"))
    @MapKeyColumn(name = "image_type")
    @Column(name = "image_url")
    private Map<String, String> image;

    @Lob
    @Column(name = "market_data")
    private String marketData;

    @Column(name = "country_origin")
    private String countryOrigin;

    @Column(name = "genesis_date")
    private String genesisDate;

    @Column(name = "sentiment_votes_up_percentage")
    private Double sentimentVotesUpPercentage;

    @Column(name = "sentiment_votes_down_percentage")
    private Double sentimentVotesDownPercentage;

    @Column(name = "market_cap_rank")
    private Integer marketCapRank;

    @Column(name = "coingecko_rank")
    private Integer coingeckoRank;

    @Column(name = "coingecko_score")
    private Double coingeckoScore;

    @Column(name = "developer_score")
    private Double developerScore;

    @Column(name = "community_score")
    private Double communityScore;

    @Column(name = "liquidity_score")
    private Double liquidityScore;

    @Column(name = "public_interest_score")
    private Double publicInterestScore;
    private LocalDateTime lastUpdated;
}
