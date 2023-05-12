package com.hometask.citylist.controller;

import com.hometask.citylist.model.dto.LoginRequest;
import com.hometask.citylist.model.dto.LoginResponse;
import com.hometask.citylist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Elnur Mammadov
 */
@RequestMapping(path = "/v1/auth", produces = "application/json")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController extends AbstractController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }
    
}
