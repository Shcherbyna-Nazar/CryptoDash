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
public class Status {
    private String timestamp;
    @JsonProperty("error_code")
    private int errorCode;
    @JsonProperty("error_message")
    private String errorMessage;
    private int elapsed;
    @JsonProperty("credit_count")
    private int creditCount;
    private String notice;
}
