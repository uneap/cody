plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    runtimeOnly(libs.h2)
    implementation(libs.h2)
    implementation(libs.spring.context)
    //local DB는 h2이지만, 상용은 다른 것으로 변경 가능
}
