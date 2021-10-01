package com.safidence.safidence.data.model

data class ResponsePolicies(
    val body: List<PoliciesBody>,
    val status: String,
    val status_code: Int
)

data class PoliciesBody(
    val file: String,
    val id: Int,
    val title: String
)