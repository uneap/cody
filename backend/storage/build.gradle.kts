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
    runtimeOnly(libs.h2)
    implementation(libs.spring.kafka)

    implementation(projects.common.core)
    implementation(projects.resource.db)
    implementation(projects.domain.brand)
    implementation(projects.domain.product)
    implementation(projects.domain.user)
    implementation(projects.domain.seller)
}
