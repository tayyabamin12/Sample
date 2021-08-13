package com.safidence.safidence.data.model

data class ResponseRequestStatus(
    val body: List<RequestStatusBody>,
    val status: String,
    val status_code: Int
)

data class RequestStatusBody(
    val created_at: String,
    val status: String,
    val subject: String
)