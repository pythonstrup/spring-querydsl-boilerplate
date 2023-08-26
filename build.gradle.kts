plugins {
    java
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"

    // asciidoctor 플러그인 추가
    id("org.asciidoctor.jvm.convert") version "3.3.2"

    // 1. Jacoco 플러그인 추가
    id("jacoco")
}

group = "com.onebyte"
version = "0.0.1-SNAPSHOT"
val queryDslVersion = "5.0.0" // QueryDSL Version Setting
val asciidoctorExt: Configuration by configurations.creating // 2. configuration 추가

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("com.mysql:mysql-connector-j")

    // QueryDSL Implementation
    implementation ("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")


    /**
     * Test
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // RestDoc
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    // Fixture Monkey
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:0.5.3")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-jackson:0.5.3")
}

/**
 * Build Optinos
 */
val querydslDir = "src/main/generated"

sourceSets {
    getByName("main").java.srcDirs(querydslDir)
}

tasks.withType<JavaCompile> {
//    options.generatedSourceOutputDirectory = file(querydslDir)
    options.generatedSourceOutputDirectory.set(file(querydslDir))
}

tasks.named("clean") {
    doLast {
        file(querydslDir).deleteRecursively()
    }
}

// Rest Doc
val snippetsDir by extra { file("build/generated-snippets") }

tasks {

    test {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    asciidoctor {
        inputs.dir(snippetsDir)
        configurations("asciidoctorExt")
        dependsOn(test)

        // 없어도 잘 돌아감
//        doFirst {
//            delete(file("src/main/resources/static/docs"))
//        }
    }

    bootJar {
        dependsOn(asciidoctor)
        from ("build/docs/asciidoc") {
            into("static/docs")
        }
    }

    register<Copy>("copyAsciidoctor") {
        dependsOn(asciidoctor)
        from(file("$buildDir/docs/asciidoc"))
        into(file("src/main/resources/static/docs"))
    }

    build {
        dependsOn("copyAsciidoctor")
    }

    /**
     * Jacoco
     */
    // 3. 커버리지 결과를 html 파일로 가공
    jacocoTestReport {
        dependsOn(test)
    }

    // 4. 커버리지 기준을 만족하는지 확인해주는 task
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = true

                element = "CLASS"

                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.80".toBigDecimal()
                }

                // 커버리지 체크 제외 클래스 지정
                excludes = listOf(
                    "*.Config.*",
                )
            }
        }
    }
}
