import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.jpa") version "1.4.32"
}

group = "com.funcorp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.4.5")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.4.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
    implementation("org.postgresql:postgresql:42.2.19")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.4.5")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.3.0")
    testImplementation("io.kotest:kotest-property-jvm:4.3.0")

    implementation("com.github.javafaker:javafaker:1.0.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
