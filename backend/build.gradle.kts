plugins {
    id("application")
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

val mainClassName = "de.maaxgr.config.backend.MainKt"

group = "de.maaxgr"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("io.insert-koin:koin-core:${Versions.koin}")
    implementation("io.insert-koin:koin-test:${Versions.koin}")

    // ktor
    implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
    implementation("ch.qos.logback:logback-classic:${Versions.logback}")
    testImplementation("io.ktor:ktor-server-tests:${Versions.ktor}")
    implementation("io.ktor:ktor-server-cors:${Versions.ktor}")
    implementation("io.ktor:ktor-server-content-negotiation:${Versions.ktor}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}")
    implementation("io.ktor:ktor-server-auth:${Versions.ktor}")
    implementation("io.ktor:ktor-server-call-logging:${Versions.ktor}")

    // config
    implementation("com.charleskorn.kaml:kaml:${Versions.kaml}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinXSerializationJson}")

    // database
    implementation("mysql:mysql-connector-java:${Versions.mysqlConnector}")
    implementation("org.ktorm:ktorm-core:${Versions.ktorm}")
    implementation("org.ktorm:ktorm-support-mysql:${Versions.ktorm}")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.github.seratch:kotliquery:1.7.0")

    // bcrypt hashing
    implementation("org.springframework.security:spring-security-core:5.6.3")

    // fuel http
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    mainClass.set(mainClassName)
    workingDir = File("${projectDir.absolutePath}/run")
}

tasks.withType<Jar>() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes("Main-Class" to mainClassName)
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
