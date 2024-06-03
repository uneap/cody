plugins {
    `java-conventions`
    `spring-boot-conventions`
}

dependencies {
    runtimeOnly(libs.h2)
    implementation(libs.h2)
    implementation(libs.spring.context)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.spring.boot.starter.jdbc)
    //local DB는 h2이지만, 상용은 다른 것으로 변경 가능
}
