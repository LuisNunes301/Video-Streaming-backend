# ---------- STAGE 1 - Build ----------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- STAGE 2 - Runtime ----------
# Alpine + apk avoids heavy Ubuntu ffmpeg dependency pulls (often flaky on archive.ubuntu.com).
FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache ffmpeg

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]