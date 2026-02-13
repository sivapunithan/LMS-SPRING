package com.punithan_library.Service.Impl;

import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserServiceImplementation implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
