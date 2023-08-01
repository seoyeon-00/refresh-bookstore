package kr.kro.refbook.config

import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    private val dotenv = Dotenv.load()

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
            password = dbPassword,
        )
    }
}