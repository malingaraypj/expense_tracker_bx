package org.example.expense_tracker_bx.controller;

import org.example.expense_tracker_bx.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("/dashboard")
public class Dashboard {

    @GetMapping
    public ResponseEntity<String> greet(){
        return new ResponseEntity<>( "Hello", HttpStatus.OK);
    }

    @PostMapping("/expense")
    public ApiResponse<>
}
