package com.safidence.safidence.data.model

data class ResponseRequestTypes(
    val body: List<BodyRequestType>,
    val status: String,
    val status_code: Int
)

data class BodyRequestType(
    val created_at: Any,
    val id: Int,
    val is_active: Int,
    val title: String,
    val updated_at: Any
)