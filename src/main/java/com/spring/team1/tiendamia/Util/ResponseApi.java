package com.spring.team1.tiendamia.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseApi<T> {
    private boolean success;
    private String message;
    private T data;
}
