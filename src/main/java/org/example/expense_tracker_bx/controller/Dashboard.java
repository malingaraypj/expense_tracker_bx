package org.example.expense_tracker_bx.controller;

import lombok.AllArgsConstructor;
import org.example.expense_tracker_bx.dto.request.ExpenseRequest;
import org.example.expense_tracker_bx.dto.response.ApiResponse;
import org.example.expense_tracker_bx.dto.response.ExpenseResponse;
import org.example.expense_tracker_bx.entity.Expense;
import org.example.expense_tracker_bx.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class Dashboard {



}
