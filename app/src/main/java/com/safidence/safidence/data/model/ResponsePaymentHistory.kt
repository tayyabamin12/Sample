package com.safidence.safidence.data.model

data class ResponsePaymentHistory(
    val body: List<BodyPaymentHistory>,
    val status: String,
    val status_code: Int,
    val message: String
)

data class BodyPaymentHistory(
    val Payment: String,
    val paid_on: String,
    val payment_method: String
)