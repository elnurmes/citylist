package com.hometask.citylist.model.dto;

import lombok.Data;

/**
 * @author Elnur Mammadov
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
