FROM openjdk:17
LABEL maintainer="valerijgoncharov"
ADD target/myProject-0.0.1-SNAPSHOT.jar nastolki.jar
ENTRYPOINT ["java", "-jar", "nastolki.jar"]