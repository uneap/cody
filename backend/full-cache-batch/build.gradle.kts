plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    compileOnly(libs.lombok)
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    implementation(libs.slf4j)
    implementation(libs.spring.boot.starter.batch)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.spring.devtool)

    implementation(projects.domain.store)
    implementation(projects.common.core)
    implementation(projects.domain.storeCache)
    implementation(projects.resource.db)
    implementation(projects.resource.redis)
}
