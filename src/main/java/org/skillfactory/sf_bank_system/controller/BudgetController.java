package org.skillfactory.sf_bank_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.skillfactory.sf_bank_system.model.dto.BudgetDto;
import org.skillfactory.sf_bank_system.model.dto.CreateBudgetDto;
import org.skillfactory.sf_bank_system.security.JwtTokenProvider;
import org.skillfactory.sf_bank_system.service.BudgetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "addBudget", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addBudget(@RequestHeader("Authorization")String authorizationHeader,
                                            @Valid @RequestBody CreateBudgetDto createBudgetDto) {

        budgetService.createBudget(
                jwtTokenProvider.getIdFromAuthorizationHeader(authorizationHeader),
                createBudgetDto
        );
        return ResponseEntity.ok().body("Budget added successfully");
    }


}
