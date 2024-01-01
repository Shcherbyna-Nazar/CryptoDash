package com.example.cryptoapp.remote.service;

import com.example.cryptoapp.remote.dto.*;
import com.example.cryptoapp.remote.model.Coin;
import com.example.cryptoapp.remote.model.CoinDetail;
import com.example.cryptoapp.remote.repository.CoinDetailRepository;
import com.example.cryptoapp.remote.repository.CoinRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoinMarketCapService {
    private static final Logger log = LoggerFactory.getLogger(CoinMarketCapService.class);

    @Value("${coinmarketcap.api.key}")
    private String apiKey;

    @Value("${coinmarketcap.api.url}")
    private String apiUrl;

    @Value("${coingecko.api.url}")
    private String apiGecko;

    @Value("${coingecko.api.key}")
    private String apiGeckoKey;

    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private CoinDetailRepository coinDetailRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private static final Duration CACHE_DURATION = Duration.ofMinutes(1);

    private final RestTemplate restTemplate;

    public CoinMarketCapService(RestTemplate restTemplate, CoinRepository coinRepository, CoinDetailRepository coinDetailRepository) {
        this.restTemplate = restTemplate;
        this.coinRepository = coinRepository;
        this.coinDetailRepository = coinDetailRepository;
    }

    public Page<Coin> getAllCoins(int page, int size, String sortBy) {
        if (shouldUpdateCache()) {
            fetchAndSaveAllCoins();
        }
        log.trace("Fetching all coins from database");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("marketCapRank"));
        return coinRepository.findAll(pageRequest);
    }

    public void fetchAndSaveAllCoins() {
        String url = apiGecko + "coins/markets?vs_currency=usd&order=market_cap_desc&per_page=250&page=1&sparkline=false&locale=en";
        try {
            ResponseEntity<List<CoinDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });

            List<CoinDTO> coinDtos = response.getBody();

            if (coinDtos != null) {
                List<Coin> coins = coinDtos.stream()
                        .map(this::convertToEntity)
                        .collect(Collectors.toList());

                // Установите текущее время обновления для каждой монеты
                coins.forEach(coin -> coin.setLastUpdated(LocalDateTime.now()));
                log.trace("Saving {} coins", coins.size());
                coinRepository.saveAll(coins); // Сохраните все монеты в базе данных
            }
        } catch (RestClientException e) {
            log.error("Error fetching all coins: {}", e.getMessage());
            // Обработка ошибок, возможно, повторная попытка или уведомление
        }
    }

    private Coin convertToEntity(CoinDTO coinDto) {
        return CoinDTO.toEntity(coinDto);
    }
    private CoinDetail convertToEntity(CoinDetailDTO coinDetailDTO) {
        return CoinDetailDTO.toEntity(coinDetailDTO);
    }

    private boolean shouldUpdateCache() {
        return coinRepository.findTopByOrderByLastUpdatedDesc()
                .map(coin -> Duration.between(coin.getLastUpdated(), LocalDateTime.now()).compareTo(CACHE_DURATION) > 0)
                .orElse(true);
    }

    public CoinDetail getCoinDetail(String coinId) {
        Optional<CoinDetail> existingDetail = coinDetailRepository.findById(coinId);
        if (existingDetail.isPresent() && !shouldUpdateDetail(existingDetail.get())) {
            return existingDetail.get();
        } else {
            return fetchAndSaveCoinDetail(coinId);
        }
    }

    private boolean shouldUpdateDetail(CoinDetail coinDetail) {
        return Duration.between(coinDetail.getLastUpdated(), LocalDateTime.now()).compareTo(CACHE_DURATION) > 0;
    }

    public CoinDetail fetchAndSaveCoinDetail(String coinId) {
        String url = apiGecko + "coins/" + coinId;
        try {
            ResponseEntity<CoinDetailDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, CoinDetailDTO.class);
            CoinDetailDTO detailDTO = response.getBody();
            if (detailDTO != null) {
                CoinDetail detail = convertToEntity(detailDTO);
                detail.setLastUpdated(LocalDateTime.now());
                return coinDetailRepository.save(detail);
            }
        } catch (RestClientException e) {
            log.error("Error fetching coin details for {}: {}", coinId, e.getMessage());
        }
        return null;
    }
}
