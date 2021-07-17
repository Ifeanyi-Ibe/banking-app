package com.phen.bankingapp.config;

import com.phen.bankingapp.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {

    private String name;
    private String password;
    private String accountNumber;
    private User user;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public UserDetailsImpl( String name, String password,
                           Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.name = name;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
    }

    public static UserDetails buildUserDetail(User user){
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorityList
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
        return true;
    }

    public User getUser() {
        return user;
    }
}
