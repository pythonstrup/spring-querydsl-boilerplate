FROM gradle:8.2.1-jdk17-alpine as builder
WORKDIR /app

# Gradle이 변경되었을 때만 의존성 패키지 다운로드 받도록 함
COPY build.gradle.kts settings.gradle.kts /app/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 빌더 이미지에서 애플리케이션 빌드
COPY . /app
RUN gradle build -x test --parallel

FROM openjdk:17-alpine
ARG jar_name=spring-boilerplate-0.0.1-SNAPSHOT

WORKDIR /app
COPY --from=builder /app/build/libs/${jar_name}.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boilerplate-0.0.1-SNAPSHOT.jar"]
