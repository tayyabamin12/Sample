package com.safidence.safidence.data.model

data class ResponseAllDocuments(
    val body: List<DocBody>,
    val status: String,
    val status_code: Int
)

data class DocBody(
    val document_id: Int,
    val expiry_date: String,
    val image: String,
    val issue_country: String,
    val number: String,
    val storage_path: String,
    val type: DocType
)

data class DocType(
    val id: Int,
    val name: String
)