plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    implementation(projects.resource.db)
    implementation(projects.resource.kafka)
    compileOnly(libs.lombok)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.kafka)
    runtimeOnly(libs.h2)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
}
