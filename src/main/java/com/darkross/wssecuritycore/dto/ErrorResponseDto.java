package com.darkross.wssecuritycore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private String errorCode;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
    private List<FieldErrorDto> fieldErrors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldErrorDto {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}

