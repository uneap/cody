plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    implementation(projects.resource.db)
    implementation(projects.common.core)
    implementation(libs.jackson)
    compileOnly(libs.lombok)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.spring.boot.starter.jdbc)
    runtimeOnly(libs.h2)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
}
