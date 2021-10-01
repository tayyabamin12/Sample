package com.safidence.safidence.data.model

data class ResponseContract(
    val body: BodyContract,
    val status: String,
    val status_code: Int
)

data class BodyContract(
    val expiry_date: String,
    val `file`: String
)