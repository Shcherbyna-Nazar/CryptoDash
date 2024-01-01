package com.example.cryptoapp.remote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TickerDTO {

    private double last;
    private double volume;

    @JsonProperty("converted_last")
    private Map<String, Double> convertedLast;

    @JsonProperty("converted_volume")
    private Map<String, Double> convertedVolume;

    private LocalDateTime timestamp;

    @JsonProperty("last_traded_at")
    private LocalDateTime lastTradedAt;

    @JsonProperty("last_fetch_at")
    private LocalDateTime lastFetchAt;

    @JsonProperty("is_stale")
    private boolean isStale;

    @JsonProperty("is_anomaly")
    private boolean isAnomaly;
}
