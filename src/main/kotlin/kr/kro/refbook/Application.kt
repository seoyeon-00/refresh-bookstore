package kr.kro.refbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kr.kro.refbook.config.DatabaseConfig

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    DatabaseConfig.init()
    runApplication<Application>(*args)
}
