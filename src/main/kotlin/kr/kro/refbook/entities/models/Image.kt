package kr.kro.refbook.entities.models

import kr.kro.refbook.entities.tables.Images
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Image(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Image>(Images)

    var fileName by Images.fileName
    var fileUrl by Images.fileUrl
    var createdAt by Images.createdAt
}
