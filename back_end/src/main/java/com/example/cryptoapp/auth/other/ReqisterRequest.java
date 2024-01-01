package com.example.cryptoapp.auth.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
