FROM maven:3.8.3-openjdk-17 AS builder

COPY ./src/ /root/src
COPY ./pom.xml /root/
# COPY ./checkstyle.xml /root/

WORKDIR /root

RUN mvn package
RUN java -Djarmode=layertools -jar /root/target/ems-simulator-0.0.1-SNAPSHOT.jar list
RUN java -Djarmode=layertools -jar /root/target/ems-simulator-0.0.1-SNAPSHOT.jar extract
RUN ls -l /root

FROM openjdk:17-jdk-slim-buster

ENV TZ=UTC
ENV DB_IP=mysqldb_device
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=admin
ENV DB_DBNAME=ems_device
ENV RMQ_IP=ems_rabbitmq
ENV RMQ_PORT=5672
COPY --from=builder /root/dependencies/ ./
COPY --from=builder /root/snapshot-dependencies/ ./
RUN sleep 10
COPY --from=builder /root/spring-boot-loader/ ./
COPY --from=builder /root/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher","-XX:+UseContainerSupport -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Xms512m -Xmx512m -XX:+UseG1GC -XX:+UseSerialGC -Xss512k -XX:MaxRAM=72m"]