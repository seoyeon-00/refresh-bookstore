package kr.kro.refbook.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.time.LocalDate

data class ProductDto(
    val id: Int,

    @field:NotNull
    val categoryId: Int,

    @field:NotBlank
    @field:Length(min = 1, max = 100)
    val title: String,

    @field:NotBlank
    @field:Length(min = 1, max = 50)
    val author: String,

    @field:NotBlank
    @field:Length(min = 1, max = 50)
    val publisher: String,

    @field:NotNull
    val publicationDate: LocalDate,

    @field:NotBlank
    @field:Pattern(regexp = "^(97([89]))?\\d{9}(\\d|X)$") // ISBN-10 or ISBN-13
    val isbn: String,

    @field:NotBlank
    @field:Length(min = 1, max = 2000)
    val description: String,

    @field:NotNull
    @field:DecimalMin("0.00")
    val price: BigDecimal,

    @field:Length(max = 200)
    val imagePath: String,

    @field:NotNull
    val isBestSeller: Boolean,
)