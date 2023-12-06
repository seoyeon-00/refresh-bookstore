package kr.kro.refbook.dto

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class ImageDto(
    val id: Int,

    @field:NotBlank
    val fileName: String,

    @field:NotBlank
    val fileUrl: String,

    val createdAt: LocalDateTime?,
)
