package org.example.expense_tracker_bx.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.expense_tracker_bx.dto.request.ExpenseRequest;
import org.example.expense_tracker_bx.dto.response.ApiResponse;
import org.example.expense_tracker_bx.dto.response.ExpenseResponse;
import org.example.expense_tracker_bx.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
@AllArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getAllExpenses(){
        return ResponseEntity.ok(ApiResponse.ok(expenseService.getAllExpenses()));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpense(@PathVariable long expenseId){
        return ResponseEntity.ok(ApiResponse.ok(expenseService.getExpense(expenseId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(@RequestBody ExpenseRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(expenseService.createExpense(request)));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(@PathVariable long expenseId, @RequestBody ExpenseRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.ok(expenseService.updateExpense(request)));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable long expenseId){
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

}
