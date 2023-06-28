package com.example.demo.services

import com.example.demo.entities.Category
import com.example.demo.dto.CategoryDto
import com.example.demo.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun getAllCategories(): List<CategoryDto> = categoryRepository.findAll().map { category -> toDto(category) }

    fun getCategoryById(id: Int): CategoryDto? = categoryRepository.findById(id)?.let { toDto(it) }

    fun createCategory(categoryDto: CategoryDto): CategoryDto = categoryRepository.create(categoryDto.name, categoryDto.categoryId).let { toDto(it) }

    fun updateCategory(id: Int, categoryDto: CategoryDto): CategoryDto? = categoryRepository.update(id, categoryDto.name, categoryDto.categoryId)?.let { toDto(it) }

    fun deleteCategory(id: Int): Boolean = categoryRepository.delete(id)

    private fun toDto(category: Category): CategoryDto = CategoryDto(
        id = category.id.value,
        name = category.name,
        categoryId = category.categoryId
    )
}
