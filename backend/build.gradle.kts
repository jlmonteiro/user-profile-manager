plugins {
    java
    alias(libs.plugins.quarkus)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom))
    implementation(libs.quarkus.resteasy.reactive)
    implementation(libs.quarkus.resteasy.reactive.jackson)
    implementation(libs.quarkus.hibernate.orm.panache)
    implementation(libs.quarkus.jdbc.postgresql)
    implementation(libs.quarkus.liquibase)
    implementation(libs.quarkus.smallrye.health)
    implementation(libs.quarkus.container.image.docker)
    implementation(libs.quarkus.config.yaml)
    implementation(libs.quarkus.hibernate.validator)
    implementation(libs.quarkus.rest.client.reactive.jackson)
    implementation(libs.uuid.creator)

    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.quarkus.cucumber)
    testImplementation(libs.junit.platform.suite)
    testImplementation(libs.rest.assured)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.junit.platform.engine)
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
