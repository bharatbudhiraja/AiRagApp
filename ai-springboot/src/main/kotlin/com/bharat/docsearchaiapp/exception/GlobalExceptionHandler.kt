package com.bharat.docsearchaiapp.exception

import com.bharat.docsearchaiapp.dto.response.ApiError
import com.bharat.docsearchaiapp.dto.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val error = ApiError(
            code = "INTERNAL_SERVER_ERROR",
            message = ex.message ?: "Something went wrong"
        )

        return ResponseEntity(
            ApiResponse(false, null, error),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        val error = ApiError(
            code = "BAD_REQUEST",
            message = ex.message ?: "Invalid request"
        )

        return ResponseEntity(
            ApiResponse(false, null, error),
            HttpStatus.BAD_REQUEST
        )
    }
}