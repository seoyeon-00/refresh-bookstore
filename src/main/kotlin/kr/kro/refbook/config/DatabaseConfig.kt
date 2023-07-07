package kr.kro.refbook.config

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kr.kro.refbook.entities.Users

val dotenv: Dotenv = dotenv()

object DatabaseConfig {
    fun init() {
        val dbHost: String = dotenv["DB_HOST"] ?: ""
        val dbPort: String = dotenv["DB_PORT"] ?: ""
        val dbName: String = dotenv["DB_NAME"] ?: ""
        val dbUser: String = dotenv["DB_USER"] ?: ""
        val dbPassword: String = dotenv["DB_PASSWORD"] ?: ""

        Database.connect(
            url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

        // 데이터베이스 트랜잭션 블록 내에서 테이블 생성
        transaction {
            SchemaUtils.create(Users)
        }
    }
}

