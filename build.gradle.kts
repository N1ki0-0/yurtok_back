
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)                             //
    implementation(libs.ktor.server.netty)                            //
    implementation(libs.logback.classic)                              //
    implementation("io.ktor:ktor-server-call-logging:3.0.3")
    implementation(libs.ktor.server.core)                             //
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    implementation("commons-codec:commons-codec:1.15")                // Для хеширования паролей
    // serialization
    implementation(libs.ktor.server.content.negotiation)              //
    implementation(libs.ktor.serialization.kotlinx.json)              //
    // JWT
    implementation(libs.ktor.server.auth)                             //
    implementation(libs.ktor.server.auth.jwt)                         //
    //Postgresql
    implementation("org.jetbrains.exposed:exposed-core:0.58.0")       //
    implementation("org.jetbrains.exposed:exposed-dao:0.58.0")        //
    implementation("org.jetbrains.exposed:exposed-jdbc:0.58.0")       //
    implementation("org.jetbrains.exposed:exposed-java-time:0.58.0")
    implementation("org.postgresql:postgresql:42.7.2")                //
    implementation("com.zaxxer:HikariCP:6.2.0")
}

//tasks.create("stage"){
//    dependsOn("installDist")
//}