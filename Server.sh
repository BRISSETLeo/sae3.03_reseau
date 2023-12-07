javac -d bin server/*.java sql/*.java
java -cp bin:/usr/share/java/mariadb-java-client.jar server/Server.java