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
    runtimeOnly(libs.h2)

    implementation(projects.resource.db)
    implementation(projects.domain.store)
    implementation(projects.domain.users)
}
