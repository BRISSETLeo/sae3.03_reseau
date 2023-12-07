javac -d bin --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls,javafx.graphics graphique/*.java graphique/controller/*.java graphique/page/*.java client/client/*.java
java -cp bin:/usr/share/java/mariadb-java-client.jar --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls,javafx.graphics graphique.Main
