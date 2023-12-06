package kr.kro.refbook.services

import kr.kro.refbook.dto.ImageDto
import kr.kro.refbook.entities.tables.Images
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime
import java.io.File
import java.util.UUID

@Service
class ImageService() {

    private val uploadDir = "uploads"

    init {
        File(uploadDir).mkdirs()
    }

    fun createImage(file: MultipartFile): ImageDto {
        return transaction {
            val fileName = file.originalFilename ?: throw IllegalArgumentException("File name not found")
            val fileUrl = saveFile(file)

            val createdAt = LocalDateTime.now()

            val imageId = Images
                .insertAndGetId {
                    it[Images.fileName] = fileName
                    it[Images.fileUrl] = fileUrl
                    it[Images.createdAt] = createdAt
                }

            return@transaction ImageDto(imageId.value, fileName, fileUrl, createdAt)
        }   
    }

    
    private fun sanitizeFilename(filename: String): String {
        return filename.replace(Regex("[^a-zA-Z0-9.-]"), "_")
    }

    private fun saveFile(file: MultipartFile): String {
        val sanitizedFilename = sanitizeFilename(file.originalFilename ?: "unknown-file")
        
        // 파일명에 UUID 랜덤 난수 추가
        val randomUUID = UUID.randomUUID().toString()
        val filenameWithUUID = "$randomUUID-$sanitizedFilename"
        
        val filePath = "$uploadDir/$filenameWithUUID"
        val absolutePath = File(filePath).absoluteFile

        file.transferTo(absolutePath)

        return absolutePath.toURI().toString()
    }

}
