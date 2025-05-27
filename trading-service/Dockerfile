FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY trading-service/pom.xml trading-service/

RUN ./mvnw dependency:go-offline -B

COPY trading-service/src trading-service/src
RUN ./mvnw package -pl trading-service -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/trading-service/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"] 