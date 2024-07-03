package com.starlingapp.roundup.exception;

public record ErrorResponse(
        int statusCode,

        String message
) {}
