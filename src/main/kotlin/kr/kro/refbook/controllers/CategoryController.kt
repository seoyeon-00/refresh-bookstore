package kr.kro.refbook.controllers

import kr.kro.refbook.dto.CategoryDto
import kr.kro.refbook.services.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryDto>> {
        return ResponseEntity.ok(categoryService.getAllCategories())
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Int): ResponseEntity<CategoryDto> {
        return categoryService.getCategoryById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createCategory(@RequestBody categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto))
    }

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Int, @RequestBody categoryDto: CategoryDto): ResponseEntity<CategoryDto> {
        return categoryService.updateCategory(id, categoryDto)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Int): ResponseEntity<Void> {
        return if (categoryService.deleteCategory(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
