package com.hometask.citylist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.EnumType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author Elnur Mammadov
 */
@Table(name = "\"user\"")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {

    private static final long serialVersionUID = -25822477129613575L;
    public interface Credentials {
        String getPassword();
    }

    public static Credentials createCredentials(String password) {
        return new Credentials() {
            @Override
            public String getPassword() {
                return password;
            }
        };
    }

    public enum Status {
        ACTIVE,
        DISABLED
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole().getPrivileges();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getStatus() == Status.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getStatus() == Status.ACTIVE;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @NotNull
    @Size(max = 50)
    private String id;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status")
    private Status status;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role")
    private Role role;
    @Column(name = "last_name")
    @NotNull
    @Size(max = 50)
    private String surname;
    @Column(name = "first_name")
    @NotNull
    @Size(max = 50)
    private String name;
    @Column(name = "email")
    @NotNull
    @Email
    @Size(max = 50)
    private String email;
    @Column(name = "password")
    @NotNull
    @Size(max = 90)
    private String password;
}
