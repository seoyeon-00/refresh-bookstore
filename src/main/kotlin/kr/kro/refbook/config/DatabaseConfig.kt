package kr.kro.refbook.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.jetbrains.exposed.sql.Database

@Component
class DatabaseConfig {

    @Value("\${db.host}")
    private lateinit var dbHost: String

    @Value("\${db.port}")
    private lateinit var dbPort: String

    @Value("\${db.name}")
    private lateinit var dbName: String

    @Value("\${db.user}")
    private lateinit var dbUser: String

    @Value("\${db.password}")
    private lateinit var dbPassword: String

    @PostConstruct
    fun init() {
        Database.connect(
            url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword,
        )
    }
}
