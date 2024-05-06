# RACE GAME
## by danyellt

### Installing for Unix
1. install Java
2. specify the location of static files folder in ```server.properties``` file in server/src/resources
3. (OPTIONAL) for saving top scores, install psql and specify the server details in ```db.properties``` file in server/src/resources
4. run ```source MavenInstall.sh```
5. run ```sh install.sh```

### Installing for Windows
1. install Java
2. install Maven manually
3. specify the location of static files folder in ```server.properties``` file in server/src/resources
4. (OPTIONAL) for saving top scores, install psql and specify the server details in ```db.properties``` file in server/src/resources
5. run ```mvn -f race/pom.xml install```
6. run ```mvn -f server/pom.xml package```

### Uninstalling for Unix
1. run ```sh MavenInstall.sh```
1. run ```sh uninstall.sh```

### Uninstalling for other OS
1. install Maven
2. run ```mvn -f race/pom.xml clean```
4. run ```mvn -f server/pom.xml clean```

### Running
1. run ```java -jar /server/target/server.jar```
2. go to localhost:8000 in browser
3. play!

### For testing
1. install Maven
2. run ```mvn -f race/pom.xml test```
3. run ```mvn -f race/pom.xml jacoco:report```
3. check coverage in target/site/jacoco/index.html

### How to play
1. Press ```Start``` to launch the game
2. Press ```A D``` or ```ArrowLeft ArrowRight``` keys for moving left or right respectively
3. Press ```W S``` or ```ArrowUp ArrowDown``` keys for speeding up or slowing down respectively
4. Press ```PAUSE` for pausing
5. Press ```STOP``` for stopping the game
6. Reloading or closing the page stops the game
7. For each car passed, player gains one point
8. For each 5 points, player gets on a new level (up to 10).
9. With each level, base speed increases.
10. Game runs even without working database, but then highscore is only relevant within current session of the server