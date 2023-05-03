package com.techacademy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.techacademy.entity.Authentication;
import com.techacademy.entity.Employee;
import com.techacademy.repository.AuthenticationRepository;

@Service
public class UserDetailService implements UserDetailsService {
    private final AuthenticationRepository authenticationRepository;

    public UserDetailService(AuthenticationRepository repository) {
        this.authenticationRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
        Optional<Authentication> authentication = authenticationRepository.findById(code);

        if (!authentication.isPresent()) {
            throw new UsernameNotFoundException("Exception:Username Not Found");
        }
        Employee employee = authentication.get().getEmployee();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(authentication.get().getRole().toString()));
        return new UserDetail(employee, authorities);
    }
}
