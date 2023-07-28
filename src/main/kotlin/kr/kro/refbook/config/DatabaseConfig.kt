package kr.kro.refbook.config

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

val dotenv: Dotenv = dotenv()

object DatabaseConfig {
    fun init() {
        val dbHost: String = dotenv["DB_HOST"] ?: ""
        val dbPort: String = dotenv["DB_PORT"] ?: ""
        val dbName: String = dotenv["DB_NAME"] ?: ""
        val dbUser: String = dotenv["DB_USER"] ?: ""
        val dbPassword: String = dotenv["DB_PASSWORD"] ?: ""

        // val dbHost: String = System.getenv("DB_HOST") ?: ""
        // val dbPort: String = System.getenv("DB_PORT") ?: ""
        // val dbName: String = System.getenv("DB_NAME") ?: ""
        // val dbUser: String = System.getenv("DB_USER") ?: ""
        // val dbPassword: String = System.getenv("DB_PASSWORD") ?: ""

        Database.connect(
            url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword,
        )

    }
}
