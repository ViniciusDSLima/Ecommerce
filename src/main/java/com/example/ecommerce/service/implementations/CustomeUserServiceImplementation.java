package com.example.ecommerce.service.implementations;

import com.example.ecommerce.entity.Users;
import com.example.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomeUserServiceImplementation implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(username);

        if(users == null){
            throw new UsernameNotFoundException("user not found with email - " + username);
        }

        List<GrantedAuthority> auth = new ArrayList<>();

        return new User(users.getEmail(), users.getPassword(), auth);
    }

}