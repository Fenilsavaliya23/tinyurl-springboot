package com.FirstProject.TinyURL.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ErrorResponseDto {

    private int statuscode;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;

}
