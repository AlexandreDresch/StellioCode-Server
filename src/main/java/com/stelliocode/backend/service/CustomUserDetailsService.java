package com.stelliocode.backend.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Substitua pelo carregamento de dados do banco de dados
        if ("testuser".equals(username)) {
            return new User("testuser", "{noop}password", new ArrayList<>());
        }
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}
