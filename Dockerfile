FROM maven:3.6.3-adoptopenjdk-14 AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM adoptopenjdk/openjdk13:alpine
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/Bootybot-1.0-SNAPSHOT-jar-with-dependencies.jar /app/BootyBot.jar
WORKDIR /app
CMD ["java", "-jar", "BootyBot.jar"]