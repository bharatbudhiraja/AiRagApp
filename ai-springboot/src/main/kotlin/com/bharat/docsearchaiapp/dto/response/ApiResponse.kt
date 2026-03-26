package com.bharat.docsearchaiapp.dto.response

import java.time.LocalDateTime

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: ApiError?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)