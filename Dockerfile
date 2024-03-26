FROM tomcat:10.0

WORKDIR /usr/local/tomcat

# Copy the WAR file into the Tomcat webapps directory
COPY ./target/JavaRushProjectOne.war /usr/local/tomcat/webapps/JavaRushProjectOne.war
COPY ./project.properties /usr/local/tomcat/project.properties

EXPOSE 8080

CMD ["catalina.sh", "run"]