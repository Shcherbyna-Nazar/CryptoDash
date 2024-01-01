package com.example.cryptoapp.remote.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CryptoQuote {
    private int id;
    private String name;
    private String symbol;
    private int isActive;
    private int isFiat;
    private List<QuoteWrapper> quotes;
}
