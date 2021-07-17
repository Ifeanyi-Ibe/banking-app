package com.phen.bankingapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseData {
    private int responseCode;
    private boolean success;
    private String message;
}
