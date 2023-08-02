package kr.kro.refbook.services

import kr.kro.refbook.dto.CategoryDto
import kr.kro.refbook.entities.models.Category
import kr.kro.refbook.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun getAllCategories(page: Int, size: Int): List<CategoryDto> {
        return categoryRepository.findAll(page, size)
            .map { category -> toDto(category) }
    }

    fun getCategoryById(id: Int): CategoryDto? = categoryRepository.findById(id)?.let { toDto(it) }

    fun createCategory(categoryDto: CategoryDto): CategoryDto = toDto(categoryRepository.create(categoryDto.name))

    fun updateCategory(id: Int, categoryDto: CategoryDto): CategoryDto? = categoryRepository.update(id, categoryDto.name)?.let { toDto(it) }

    fun deleteCategory(id: Int): Boolean = categoryRepository.delete(id)

    private fun toDto(category: Category): CategoryDto = CategoryDto(
        id = category.id.value,
        name = category.name,
    )
}
