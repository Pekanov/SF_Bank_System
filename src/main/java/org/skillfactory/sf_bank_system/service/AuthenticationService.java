package org.skillfactory.sf_bank_system.service;


import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.entity.User;
import org.skillfactory.sf_bank_system.model.dto.AuthorisationDto;
import org.skillfactory.sf_bank_system.model.dto.JwtTokensDto;
import org.skillfactory.sf_bank_system.model.dto.RegisterDto;
import org.skillfactory.sf_bank_system.repository.UserRepository;
import org.skillfactory.sf_bank_system.repository.WalletRepository;
import org.skillfactory.sf_bank_system.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final WalletService walletService;


    public JwtTokensDto registerUser(RegisterDto registerDto){
        if(userRepository.findByEmail(registerDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("User with email: " + registerDto.getEmail() + "already exist");
        }
        try {
  
            User newUser = User.builder().
                    email(registerDto.getEmail()).
                    password(passwordEncoder.encode(registerDto.getPassword())).
                    build();

            userRepository.save(newUser);
            walletService.createWallet(newUser);

            return createTokensForUser(newUser);
        }catch (Exception e){
            throw new RuntimeException("User registration is not successful", e);
        }
    }

    public JwtTokensDto loginUser(AuthorisationDto authorisationDto) {
        User user = userRepository.findByEmail(authorisationDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User with email: " + authorisationDto.getEmail() + " not exist"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername() , authorisationDto.getPassword()
        ));
        return createTokensForUser(user);
    }

    private JwtTokensDto createTokensForUser(User user) {
        return new JwtTokensDto(jwtTokenProvider.generateAccessToken(user),
                jwtTokenProvider.generateRefreshToken(user));
    }

    public JwtTokensDto refreshToken(String refreshToken) {
        String username = jwtTokenProvider.getUsernameFromJwt(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User with email: " + username + " not exist"));
        return createTokensForUser(user);
    }


}

