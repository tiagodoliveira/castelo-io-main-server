package io.castelo.main_server.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails{

    @Id
    @NotNull
    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @NotBlank
    @Column(name = "email" , nullable = false, columnDefinition = "text", unique = true)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false, columnDefinition = "text")
    private String password;

    @NotBlank
    @Column(name = "display_name", nullable = false, columnDefinition = "text")
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "text")
    private UserRoles role;

    @Column(name = "is_credentials_non_expired", nullable = false, columnDefinition = "boolean")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_user_enabled", nullable = false, columnDefinition = "boolean")
    private boolean isUserEnabled;

    public User(@NotNull UUID userId, @NotBlank String email, @NotBlank String password, String displayName, UserRoles role, boolean isCredentialsNonExpired, boolean isUserEnabled) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.role = role;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isUserEnabled = isUserEnabled;
    }

    public User(@NotNull String userId, @NotBlank String email, @NotBlank String password, String displayName, UserRoles role, boolean isCredentialsNonExpired, boolean isUserEnabled) {
        this(UUID.fromString(userId), email, password, displayName, role, isCredentialsNonExpired, isUserEnabled);
    }

    public User() {
    }

    @PrePersist
    @PreUpdate
    private void normalizeEmailAndSetDefaultRole() {
        this.email = email.toLowerCase();
        if (this.role == null) {
            setRole(UserRoles.USER);
        }
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@NotNull UUID userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.toLowerCase();
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null) {
            return Collections.emptyList();
        }
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public void setDisplayName(@NotBlank String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isUserEnabled;
    }

    public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    public void setUserEnabled(boolean isUserEnabled) {
        this.isUserEnabled = isUserEnabled;
    }

    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
