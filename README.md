GraphQL GeocoderService
=======================

GraphQL Web Service for pgGeocoder. This gives the user the ability to choose
and receive only the required fields when doing a Geocoder or Reverse Geocoder
requests. 


### Build
Use Maven to build and create a WAR file by:

```
  mvn clean install
```

### Test
Run the following command to test the application before deployment

```
mvn spring-boot:run
```

### View Interactive GraphQL
To view the GraphQL Integrated Development Environment tool, view:

```
http://localhost:8080/graphiql
```

### Deploy in a Tomcat Application Server

Copy the created WAR file into the [TomcatDir]/webapps directory and start Tomcat. 
Then edit the properties file for the correct database connection settings.


### Deploy in a Serverless Environment

To run the application in a serverless environment (no Application Server), do:

```
java -jar geocoderGQLService.war
```


### Sample Curl Requests

* For Geocoder Requests

```shell
curl http://localhost:8080/graphql -X POST -H 'content-type: application/json' -d '{"query":"query{  geocoder(addr:\"東京都杉並区荻窪一丁目7-13\"){code address todofuken shikuchoson ooaza chiban go coordinates{x y}}}"}'            
```

  **Result**

```json
{"data":{"geocoder":{"code":1,"address":"東京都杉並区荻窪一丁目7-13","todofuken":"東京都","shikuchoson":"杉並区","ooaza":"荻窪一丁目","chiban":"7","go":"13","coordinates":{"x":139.62062072753906,"y":35.69243621826172}}}}
```

* For Reverse Geocoder Requests

```shell
curl http://localhost:8080/graphql -X POST -H 'content-type: application/json' -d '{"query":"query{  reverseGeocoder(x:139.61984252929688 y:35.71017837524414){code address todofuken shikuchoson ooaza chiban go coordinates{y x}}}"}'
```

  **Result**
  
```json
{"data":{"reverseGeocoder":{"code":1,"address":"東京都杉並区清水一丁目3-4","todofuken":"東京都","shikuchoson":"杉並区","ooaza":"清水一丁目","chiban":"3","go":"4","coordinates":{"y":35.71017837524414,"x":139.61984252929688}}}}
```

