package com.safidence.safidence.data.model

data class ResponseDuePayment(
    val body: BodyDuePayment,
    val status: String,
    val status_code: Int
)

data class BodyDuePayment(
    val due_date: String,
    val pay_till: String,
    val rent: String
)