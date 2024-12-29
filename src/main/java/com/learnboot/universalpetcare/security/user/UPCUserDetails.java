package com.learnboot.universalpetcare.security.user;

import com.learnboot.universalpetcare.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UPCUserDetails implements UserDetails {

    private long id;
    private String email;
    private String password;
    private boolean isEnabled;
    private Collection<GrantedAuthority> authorities;

    public static UPCUserDetails buildUserDetails(User user){
        List<GrantedAuthority> authoritiesList =
                user.getRoles().stream().map(role->new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList());
        return new UPCUserDetails(user.getId(), user.getEmail(), user.getPassword(),user.isActive(), authoritiesList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
