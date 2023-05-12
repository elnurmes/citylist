package com.hometask.citylist.service;

import com.hometask.citylist.config.jwt.JwtTokenGenerator;
import com.hometask.citylist.model.User;
import com.hometask.citylist.model.dto.LoginRequest;
import com.hometask.citylist.model.dto.LoginResponse;
import com.hometask.citylist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

/**
 * @author Elnur Mammadov
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Transactional(rollbackFor = Throwable.class)
    public void changePassword(User user, String currentPassword, String newPassword, String newPassword2) throws ValidationException {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ValidationException("invalid current password specified");
        }
        if (!newPassword.equals(newPassword2)) {
            throw new ValidationException("passwords don't match");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            String username = request.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            String token = jwtTokenGenerator.createToken(username, this.userRepository.findByEmail(username).getRole().toString());

            return new LoginResponse(username, token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }
}
