package kr.kro.refbook.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ProductDto(
    val id: Int,
    val categoryId: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val publicationDate: LocalDate,
    val isbn: String,
    val description: String,
    val price: BigDecimal,
    val imagePath: String,
    val isBestSeller: Boolean
)
