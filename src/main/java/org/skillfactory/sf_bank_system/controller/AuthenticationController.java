package org.skillfactory.sf_bank_system.controller;

import jakarta.validation.Valid;
import org.skillfactory.sf_bank_system.model.dto.AuthorisationDto;
import org.skillfactory.sf_bank_system.model.dto.JwtTokensDto;
import org.skillfactory.sf_bank_system.model.dto.RegisterDto;
import org.skillfactory.sf_bank_system.service.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokensDto> register (@Valid @RequestBody RegisterDto registerDto) {
        JwtTokensDto token = authenticationService.registerUser(
                registerDto
        );
        return ResponseEntity.ok().body(
                token
        );
    }

    @PostMapping(value = "/authorisation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokensDto> authorise (@Valid @RequestBody AuthorisationDto authorisationDto) {
        JwtTokensDto token = authenticationService.loginUser(
                authorisationDto
        );
        return ResponseEntity.ok().body(
                token
        );
    }

    @PostMapping(value = "/updateTokens", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokensDto> updateTokens(@Valid @RequestBody JwtTokensDto jwtTokensDto){
        String refreshToken = jwtTokensDto.getRefreshToken();
        JwtTokensDto token = authenticationService.refreshToken(
                refreshToken
        );
        return ResponseEntity.ok().body(
                token
        );
    }

}
