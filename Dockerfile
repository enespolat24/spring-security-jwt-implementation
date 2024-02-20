FROM maven:3.8.3-openjdk-17-slim AS builder
LABEL authors="enespolat24"

COPY . .

RUN mvn clean install

FROM openjdk:17-slim AS runtime
LABEL authors="enespolat24"
COPY --from=builder /target/*.jar /app.jar

CMD ["java", "-jar", "/app.jar"]