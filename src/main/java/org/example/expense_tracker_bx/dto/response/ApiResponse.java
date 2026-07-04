package org.example.expense_tracker_bx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean status;
    private String message;
    private T data;

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .status(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> ok(T data, String message){
        return ApiResponse.<T>builder()
                .status(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status(false)
                .message(message)
                .build();
    }
}