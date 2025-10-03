package com.reliaquest.api.dto;

import lombok.Data;

@Data
public class ExternalApiResponse<T> {
    private T data;
    private String status;
}