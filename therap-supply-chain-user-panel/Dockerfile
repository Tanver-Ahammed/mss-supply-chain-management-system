# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS mss_supply_chain_admin
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal JRE image
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=mss_supply_chain_admin /app/target/therap-supply-chain-admin-panel-0.0.1-SNAPSHOT.jar mss-sc_admin.jar

EXPOSE 8081

# Specify the default command to run on startup
CMD ["java", "-jar", "mss-sc_admin.jar"]