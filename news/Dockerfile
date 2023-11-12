#FROM openjdk:8
#VOLUME /tmp
#COPY ./target/news-1.0.jar news-1.0.jar
#CMD ["java","-jar","news-1.0.jar"]
#ENTRYPOINT ["java","-jar","/news-1.0.jar"]

#docker image build -t news .

FROM openjdk:8
#FROM eclipse-temurin:17-jdk-alpine
#FROM eclipse-temurin:8-jdk-alpine
# Copy the application.properties file into the container
COPY /src/main/resources/application.properties /application.properties

VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} news-1.0.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /news-1.0.jar ${0} ${@}"]
#Note the use of ${0} for the “command” (in this case the first program argument)
#and ${@} for the “command arguments” (the rest of the program arguments)

#Create network for MySQL, SpringBoot and Agular commuinication
#docker network create news-mysql-angular

#Build image docker for spring boot application
#docker build -t quan89/news .

#Run spring boot application in docker
#docker run --network news-mysql-angular --name news-container -p 8081:8081 -e "JAVA_OPTS=-Ddebug -Xmx128m" -d quan89/news --server.port=8081
#docker run -p 8081:8081 -e "JAVA_OPTS=-Ddebug -Xmx128m" quan89/news
#docker run -p 9000:9000 -e "JAVA_OPTS=-Ddebug -Xmx128m" quan89/news --server.port=9000
#docker run -v /src/main/resources/application.properties:/application.properties --name news-container


#run mysql
#docker container run --name mysqldb --network news-mysql-angular -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=springboot -d mysql:8.0.13 --lower_case_table_names=1
#connect my sql db in docker
#docker container exec -it [container_id_of_mysql] bash
#mysql -u[user] -p[password]
#mysql -uroot -p1234
#show databases;
#use springboot;
#show tables;
#select * from user;