package com.safidence.safidence.data.model

data class ResponseDocTypes(
    val `data`: List<Data>
)

data class Data(
    val id: Int,
    val name: String
)