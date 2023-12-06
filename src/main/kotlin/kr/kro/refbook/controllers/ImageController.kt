package kr.kro.refbook.controllers

import kr.kro.refbook.dto.ImageDto
import kr.kro.refbook.services.ImageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/image")
class ImageController(private val imageService: ImageService) {
    
    @PostMapping("/upload", consumes = ["multipart/form-data"])
    fun createImage(@RequestParam("file") file: MultipartFile): ResponseEntity<ImageDto> {
        val createdImageDto = imageService.createImage(file)
        return ResponseEntity.ok(createdImageDto)
    }

}
