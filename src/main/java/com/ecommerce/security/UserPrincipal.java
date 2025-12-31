package com.ecommerce.security;

import com.ecommerce.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * UserPrincipal class implementing Spring Security's UserDetails interface.
 * This class represents the principal object that contains user authentication and authorization information.
 * 
 * Created: 2025-12-31 00:11:56 UTC
 * Author: tembhreashli
 */
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean enabled;
    private List<GrantedAuthority> authorities;

    /**
     * Default constructor
     */
    public UserPrincipal() {
        this.authorities = new ArrayList<>();
        this.enabled = true;
    }

    /**
     * Constructor with username and password
     */
    public UserPrincipal(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor with full user details
     */
    public UserPrincipal(Long id, String username, String email, String password) {
        this();
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Factory method to create UserPrincipal from User entity
     */
    public static UserPrincipal create(User user) {
        UserPrincipal userPrincipal = new UserPrincipal(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword()
        );
        userPrincipal.setEnabled(user.getIsActive());
        userPrincipal.addAuthority("ROLE_" + user.getUserRole().name());
        return userPrincipal;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    // UserDetails implementation methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities != null ? authorities : new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled != null && enabled;
    }

    /**
     * Add authority to the user
     */
    public void addAuthority(String authority) {
        if (this.authorities == null) {
            this.authorities = new ArrayList<>();
        }
        this.authorities.add(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                '}';
    }
}
