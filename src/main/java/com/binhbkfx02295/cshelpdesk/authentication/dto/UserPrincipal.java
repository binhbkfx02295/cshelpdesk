package com.binhbkfx02295.cshelpdesk.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.binhbkfx02295.cshelpdesk.employee_management.employee.entity.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {
    private final String username;
    private final String fullName;
    private final String description;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Employee employee;

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { 
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(employee.getUserGroup().getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + employee.getUserGroup().getName()));
        return authorities;
     }
    @Override public boolean isAccountNonLocked() { return employee.isActive(); }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }
}
