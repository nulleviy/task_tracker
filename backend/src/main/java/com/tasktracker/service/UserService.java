package com.tasktracker.service;

import com.tasktracker.dto.CompositeUserResponse;
import com.tasktracker.dto.UserRequest;
import com.tasktracker.dto.UserResponse;
import com.tasktracker.messaging.UserProducer;
import com.tasktracker.model.User;
import com.tasktracker.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final PasswordEncoder passEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserProducer producer;

    public UserService(JwtUtil jwtUtil, UserRepo userRepo, PasswordEncoder passEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserProducer producer) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.passEncoder = passEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.producer = producer;
    }

    @Transactional(readOnly = true)
    public UserResponse findByEmail(String email){
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent()){
            return new UserResponse(user.get().getId(),user.get().getEmail());
        }
        throw new NoSuchElementException("Нет пользователя с email: ");
    }

    @Transactional(readOnly = true)
    public User findById(Long id){
        Optional<User> user = userRepo.findById(id);
        return user.orElseThrow();
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CompositeUserResponse> userByBearer(String bearer){
        if(jwtUtil.validBearer(bearer)){
            UserResponse response = findByEmail(jwtUtil.extractUsername(bearer.substring(7)));
            return ResponseEntity.status(HttpStatus.OK).body(CompositeUserResponse.success(response,"There's your credentials"));
        } return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CompositeUserResponse.error("UNAUTHORIZED: Something wrong with your token"));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeUserResponse> registration(UserRequest request){
        if(request.getEmail()==null || request.getPassword()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeUserResponse.error("NOT FOUND: your email/password are blank"));
        }if(userRepo.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CompositeUserResponse.error("CONFLICT: this email is already taken"));
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passEncoder.encode(request.getPassword()))
                .build();
        userRepo.save(user);
        producer.sendUserCreated(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CompositeUserResponse.success(user,"User Registered Successfully your jwt access token is: "
                +jwtUtil.generateToken(
                 userDetailsService.loadUserByUsername(request.getEmail()))));

    }

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeUserResponse> authorization(UserRequest request){
        if(request.getEmail()!=null){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(CompositeUserResponse.success("You're authorization succeed here's your token: "+jwtUtil.generateToken(userDetails)));
        } return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeUserResponse.error("NOT FOUND: There's no email in your request"));
    }


}
