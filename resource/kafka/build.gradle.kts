plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    implementation(libs.spring.context)
    implementation(libs.spring.kafka)
    implementation(libs.slf4j)

    compileOnly(libs.lombok)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}
