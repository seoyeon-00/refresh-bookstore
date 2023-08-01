package kr.kro.refbook

import kr.kro.refbook.config.DatabaseConfig
import kr.kro.refbook.utils.EnvLoader
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    EnvLoader.getAllProperties().forEach {
        System.setProperty(it.key, it.value)
    }
    // init 메서드 호출
    DatabaseConfig.init()
    runApplication<Application>(*args)
}
