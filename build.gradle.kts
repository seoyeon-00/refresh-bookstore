import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "kr.kro"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    //postgressql dependencies
    implementation("org.postgresql:postgresql")

    //redis dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("org.springframework.boot:spring-boot-starter-web")

    //exposed dependencies
    implementation("org.jetbrains.exposed:exposed-java-time:0.35.1")
    implementation("org.jetbrains.exposed:exposed-core:0.35.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.35.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.35.1")

    //dotenv dependencies
    implementation("io.github.cdimascio:java-dotenv:5.1.3")
    //implementation("org.springframework.boot:spring-boot-starter-security")
    //implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
