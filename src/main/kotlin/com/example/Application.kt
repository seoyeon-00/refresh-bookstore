package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.example.demo.config.DatabaseConfig

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    DatabaseConfig.init() 
    runApplication<Application>(*args)
}
