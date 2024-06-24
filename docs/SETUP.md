## SETUP DOCUMENTATION

1. Ensure you have <b>JAVA 17 verion</b> installed. You can check the version by following command.
```
java --version 
```
2. Clone this repository locally. You can use following command to that.
```
git clone https://github.com/ParundeepSingh/yuda-platform-be-service.git
```
4. Ensure you have remote <b>MySQL DB instance</b> or have it running locally.
5. Import the project in any IDE.
6. Edit the application.properties. And add the following properties in that file.
<br>Following Database configurations needs to be added.
```
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/<DB_NAME>
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>
```

Also need to generate the Youtube Data Api v3 key and add that to in application.properties.
```
# TODO : Add youtube data api v3 key over here.
yt.data.api.v3.key=<<API_KEY>>
```

6.  Run the following command to build the project and fetch dependencies.
```
 ./gradlew clean build
```
7. On successful build, you can run the following command to spin up the service locally.
```
./gradlew bootRun 
```
