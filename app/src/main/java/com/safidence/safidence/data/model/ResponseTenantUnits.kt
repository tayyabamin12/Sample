package com.safidence.safidence.data.model

data class ResponseTenantUnits(
    val body: List<BodyTenantUnits>,
    val status: String,
    val status_code: Int
)

data class BodyTenantUnits(
    val id: Int,
    val name: String
)