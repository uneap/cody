plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    implementation(libs.spring.context)
    implementation(libs.spring.boot.starter.redis)

    implementation(libs.slf4j)
    implementation(libs.apache.commons.pool2)

    compileOnly(libs.lombok)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}
