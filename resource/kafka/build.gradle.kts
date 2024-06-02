plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    implementation(libs.spring.context)
    implementation(libs.spring.kafka)
    //local DB는 h2이지만, 상용은 다른 것으로 변경 가능
}
