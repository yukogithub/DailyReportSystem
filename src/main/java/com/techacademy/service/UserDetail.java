package com.techacademy.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.techacademy.entity.Employee;

public class UserDetail implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Employee employee;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetail(Employee employee) {
        this.employee = employee;
        this.authorities = new ArrayList<GrantedAuthority>();
    }

    public Employee getUser() {
        return employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getAuthentication().getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getAuthentication().getCode();
    }

    @Override
    public boolean isAccountNonExpired() {
        // ユーザーが期限切れでなければtrueを返す
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // ユーザーがロックされていなければtrueを返す
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // ユーザーのパスワードが期限切れでなければtrueを返す
        return true;
    }

    @Override
    public boolean isEnabled() {
        // ユーザーが有効であればtrueを返す
        return true;
    }
}