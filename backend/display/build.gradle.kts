plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    compileOnly(libs.lombok)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.jpa)

    implementation(projects.domain.storeCache)
    implementation(projects.common.core)
}
