package com.hometask.citylist.controller;

import com.hometask.citylist.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Elnur Mammadov
 */
@RequestMapping(path = "/v1/user", produces = "application/json")
@RestController
@RequiredArgsConstructor
public class UserController extends AbstractController {

    private final UserService userService;

    @Getter
    @Setter
    public static class ChangePasswordRequest {
        @NotBlank
        private String currentPassword;
        @Size(max = 50)
        @NotBlank
        private String newPassword;
        @NotBlank
        private String newPassword2;
    }

    @PostMapping("changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(
            getUserOrThrow(),
            request.getCurrentPassword(),
            request.getNewPassword(),
            request.getNewPassword2()
        );
        return new ResponseEntity<>(successReply("password has changed"), HttpStatus.OK);
    }
}
