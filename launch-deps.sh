#!/bin/bash

echo "Starting dependencies..."

docker-compose up -d

sleep 5

i=1
app="tictactoe_db"

while [ i=1 ]
do
  echo "Waiting for MySQL to start..."
  sleep 2
  if sudo docker ps | awk -v app="$app" 'NR > 1 && $NF == app{ret=1; exit} END{exit !ret}'; then
    break
  fi 
done


echo "Depencies are up! Starting App now..."

#sudo java -Ddebug -jar target/ordercomp-1.0-SNAPSHOT.jar

sleep 2

echo "Application is up. Please do 'docker ps' to confirm!"

