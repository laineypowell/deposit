plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "com.laineypowell"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.slf4j:slf4j-api:2.0.17")
    shadow("org.slf4j:slf4j-api:2.0.17")

    testImplementation("org.slf4j:slf4j-simple:2.0.17")
    shadow("org.slf4j:slf4j-simple:2.0.17")

    implementation("com.google.code.gson:gson:2.13.2")
    shadow("com.google.code.gson:gson:2.13.2")
}

tasks.test {
    useJUnitPlatform()
}