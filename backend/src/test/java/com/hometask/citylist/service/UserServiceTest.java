package com.hometask.citylist.service;

import com.hometask.citylist.model.User;
import com.hometask.citylist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ValidationException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author Elnur Mammadov
 */

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testChangePasswordWithInvalidCurrentPassword() {
        // Arrange
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String newPassword2 = "newPassword2";

        User user = new User();
        user.setPassword(passwordEncoder.encode("differentPassword"));

        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(false);

        // Act and Assert
        assertThrows(ValidationException.class, () -> userService.changePassword(user, currentPassword, newPassword, newPassword2));
    }

    @Test
    void testChangePasswordWithNonMatchingNewPasswords() {
        // Arrange
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String newPassword2 = "newPassword2";

        User user = new User();
        user.setPassword(passwordEncoder.encode(currentPassword));

        // Act and Assert
        assertThrows(ValidationException.class, () -> userService.changePassword(user, currentPassword, newPassword, newPassword2));
    }

    @Test
    void testChangePassword() throws ValidationException {
        // Arrange
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String newPassword2 = "newPassword";

        User user = new User();
        user.setPassword(passwordEncoder.encode(currentPassword));

        when(passwordEncoder.matches(currentPassword, user.getPassword())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        userService.changePassword(user, currentPassword, newPassword, newPassword2);

        // Assert
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        assertThat(user.getPassword()).isEqualTo(encodedNewPassword);
    }
}
