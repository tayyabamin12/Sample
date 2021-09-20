package com.safidence.safidence.data.model

data class ResponseUnitDetails(
    val body: List<UnitsDetailBody>,
    val status: String,
    val status_code: Int
)

data class UnitsDetailBody(
    val buliding: Buliding,
    val name: String,
    val parking: Int,
    val space: String,
    val type: String
)

data class Buliding(
    val address: String,
    val name: String
)