package org.example.expense_tracker_bx.controller;

import lombok.RequiredArgsConstructor;
import org.example.expense_tracker_bx.dto.response.DashboardSummaryDTO;
import org.example.expense_tracker_bx.entity.User;
import org.example.expense_tracker_bx.repository.UserRepository;
import org.example.expense_tracker_bx.service.DashboardService;
import org.example.expense_tracker_bx.service.impl.DashboardServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary(Principal principal) {

        if(principal == null){
            return ResponseEntity.status(401).build();
        }

        String username = principal.getName();



        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // userId should ideally be extracted from the security context or JWT token
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(user.getId());
        return ResponseEntity.ok(summary);
    }
}