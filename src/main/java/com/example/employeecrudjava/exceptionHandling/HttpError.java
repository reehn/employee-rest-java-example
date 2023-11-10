package com.example.employeecrudjava.exceptionHandling;

import lombok.Builder;

@Builder
public record HttpError(
        String title,
        int statusCode,
        String detailedMessage
) {
}
