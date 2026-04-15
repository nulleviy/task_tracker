package com.tasktracker.service;

import com.tasktracker.model.User;
import com.tasktracker.repo.UserRepo;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepo.findByEmail(email);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User with this email is not found");
        }

        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(),new ArrayList<>());
    }

}
