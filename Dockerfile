FROM payara/server-full

EXPOSE 9000

COPY target/my-app-1.0-SNAPSHOT.jar /opt/payara/deployments/my-app-1.0-SNAPSHOT.jar

