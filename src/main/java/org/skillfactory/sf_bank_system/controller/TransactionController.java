package org.skillfactory.sf_bank_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.dto.TransactionDto;
import org.skillfactory.sf_bank_system.security.JwtTokenProvider;
import org.skillfactory.sf_bank_system.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping(value = "addTransaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTransaction(@RequestHeader("Authorization")String authorizationHeader,
                                                 @Valid @RequestBody TransactionDto transactionDto) {
        transactionService.addTransaction(
                transactionDto,
                jwtTokenProvider.getIdFromAuthorizationHeader(authorizationHeader)
        );

                return ResponseEntity.ok().body("Transaction added successfully");

    }

}
