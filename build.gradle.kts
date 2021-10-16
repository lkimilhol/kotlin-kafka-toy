plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
}

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

dependencies {
    subprojects.forEach{
        implementation(it)
    }
}