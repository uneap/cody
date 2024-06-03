plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    implementation(projects.resource.redis)
    implementation(projects.common.core)

    implementation(libs.jackson)
    compileOnly(libs.lombok)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.kafka)
    implementation(libs.spring.context)
    implementation(libs.spring.boot.starter.redis)
    runtimeOnly(libs.h2)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
}
