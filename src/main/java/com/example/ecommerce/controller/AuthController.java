package com.example.ecommerce.controller;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.entity.AuthResponse;
import com.example.ecommerce.entity.Users;
import com.example.ecommerce.exception.UserException;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.request.LoginRequest;
import com.example.ecommerce.service.implementations.CustomeUserServiceImplementation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private CustomeUserServiceImplementation customeUserServiceImplementation;

    @SneakyThrows
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody Users users){
        String email = users.getEmail();
        String password = users.getPassword();
        String firstName = users.getFirstName();
        String lastName = users.getLastName();

        Users isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist != null){
            throw new UserException("Email is already used with another account");
        }

        Users createdUser = new Users();

        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);

        Users savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token, "Signup Sucess");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest request){

        String username = request.getEmail();

        String password = request.getPassword();

        Authentication authentication = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token, "Signup success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customeUserServiceImplementation.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Username");
        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
