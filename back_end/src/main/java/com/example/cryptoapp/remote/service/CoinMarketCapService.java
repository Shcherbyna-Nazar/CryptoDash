package com.example.cryptoapp.remote.service;

import com.example.cryptoapp.remote.dto.CoinDTO;
import com.example.cryptoapp.remote.dto.CoinDetailDTO;
import com.example.cryptoapp.remote.model.Coin;
import com.example.cryptoapp.remote.model.CoinDetail;
import com.example.cryptoapp.remote.model.EndpointLastUpdate;
import com.example.cryptoapp.remote.repository.CoinDetailRepository;
import com.example.cryptoapp.remote.repository.CoinRepository;
import com.example.cryptoapp.remote.repository.EndpointLastUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinMarketCapService {
    private static final Logger log = LoggerFactory.getLogger(CoinMarketCapService.class);

    @Value("${coingecko.api.url}")
    private String apiGecko;

    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private CoinDetailRepository coinDetailRepository;
    @Autowired
    private EndpointLastUpdateRepository endpointLastUpdateRepository;

    private static final Duration CACHE_DURATION = Duration.ofMinutes(1);

    private final RestTemplate restTemplate;

    public CoinMarketCapService(RestTemplate restTemplate, CoinRepository coinRepository, CoinDetailRepository coinDetailRepository) {
        this.restTemplate = restTemplate;
        this.coinRepository = coinRepository;
        this.coinDetailRepository = coinDetailRepository;
    }

    public Page<Coin> getAllCoins(int page, int size) {
        if (shouldUpdateEndpoint("getAllCoins")) {
            fetchAndSaveAllCoins();
            updateEndpointLastUpdateTime("getAllCoins");
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

                log.trace("Saving {} coins", coins.size());
                coinRepository.saveAll(coins); // Save all coins in the database
            }
        } catch (RestClientException e) {
            log.error("Error fetching all coins: {}", e.getMessage());
        }
    }

    private Coin convertToEntity(CoinDTO coinDto) {
        return CoinDTO.toEntity(coinDto);
    }

    private CoinDetail convertToEntity(CoinDetailDTO coinDetailDTO) {
        return CoinDetailDTO.toEntity(coinDetailDTO);
    }

    private boolean shouldUpdateEndpoint(String endpointName) {
        return endpointLastUpdateRepository.findByEndpointName(endpointName)
                .map(update -> Duration.between(update.getLastUpdated(), LocalDateTime.now()).compareTo(CACHE_DURATION) > 0)
                .orElse(true);
    }

    private void updateEndpointLastUpdateTime(String endpointName) {
        EndpointLastUpdate update = endpointLastUpdateRepository.findByEndpointName(endpointName)
                .orElse(new EndpointLastUpdate(null, endpointName, LocalDateTime.now()));
        update.setLastUpdated(LocalDateTime.now());
        endpointLastUpdateRepository.save(update);
    }

    public CoinDetail getCoinDetail(String coinId) {
        try {

            if (shouldUpdateEndpoint("getCoinDetail:" + coinId)) {
                CoinDetail detail = fetchAndSaveCoinDetail(coinId);
                updateEndpointLastUpdateTime("getCoinDetail:" + coinId);
                return detail;
            }
        } catch (RestClientException e) {
            log.error("Error fetching coin details for {}: {}", coinId, e.getMessage());
        }

        return coinDetailRepository.findById(coinId).orElse(null);


    }

    public CoinDetail fetchAndSaveCoinDetail(String coinId) {
        String url = apiGecko + "coins/" + coinId;
        try {
            ResponseEntity<CoinDetailDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, CoinDetailDTO.class);
            CoinDetailDTO detailDTO = response.getBody();
            if (detailDTO != null) {
                CoinDetail detail = convertToEntity(detailDTO);
                return coinDetailRepository.save(detail);
            }
        } catch (Exception e) {
            log.error("Error fetching coin details for {}: {}", coinId, e.getMessage());
            throw new RestClientException(e.getMessage());
        }
        return null;
    }
}
