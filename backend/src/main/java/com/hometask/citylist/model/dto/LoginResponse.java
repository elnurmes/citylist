package com.hometask.citylist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Elnur Mammadov
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String token;
}
