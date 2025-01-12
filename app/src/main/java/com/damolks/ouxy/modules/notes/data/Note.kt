package com.damolks.ouxy.modules.notes.data

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String,
    val title: String,
    val content: String,
    val tags: List<String>,
    val createdAt: Long,
    val updatedAt: Long
)