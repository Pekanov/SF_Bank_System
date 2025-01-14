package org.skillfactory.sf_bank_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.dto.UserFullInfoDto;
import org.skillfactory.sf_bank_system.security.JwtTokenProvider;
import org.skillfactory.sf_bank_system.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping(value = "/getUserInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserFullInfoDto> getUserInfo(@RequestHeader("Authorization")String authorizationHeader) {
        return ResponseEntity.ok().body(
                userService.getUserFullInfo(
                        jwtTokenProvider.getIdFromAuthorizationHeader(authorizationHeader)
                )
        );
    }

}
