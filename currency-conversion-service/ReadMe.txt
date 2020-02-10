- https://start.spring.io/: to creat a spring boot project
- mvn package
- execute: java -jar cunrrency-conversion-service-0.0.1-SNAPSHOT.jar
- opefn web browser and call:

rest template client:
http://localhost:8100/currency-converter/from/EUR/to/INR/quantity/10000

Feign client:
http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10000