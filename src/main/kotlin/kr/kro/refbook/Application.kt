package kr.kro.refbook

import kr.kro.refbook.config.DatabaseConfig
import kr.kro.refbook.utils.EnvLoader
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {

    @Bean
    fun runner(databaseConfig: DatabaseConfig): CommandLineRunner {
        return CommandLineRunner {
            databaseConfig.init()
        }
    }
}

fun main(args: Array<String>) {
    EnvLoader.getAllProperties().forEach {
        System.setProperty(it.key, it.value)
    }
    runApplication<Application>(*args)
}
