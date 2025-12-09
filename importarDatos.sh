#!/bin/bash

echo "Importando colecciones en MongoDB..."

# Variables de conexión
MONGO_HOST="mongodb"
MONGO_PORT="27016"
MONGO_USER="root"
MONGO_PASS="root"
MONGO_DB="TierraBurrito"

# Iterar sobre los archivos JSON y hacer import
for FILE in /data/dump/*.json
do
  COLLECTION_NAME=$(basename "$FILE" .json)
  echo "Importando colección $COLLECTION_NAME..."
  mongoimport --host $MONGO_HOST --port $MONGO_PORT \
    --username $MONGO_USER --password $MONGO_PASS \
    --authenticationDatabase admin \
    --db $MONGO_DB --collection $COLLECTION_NAME --file "$FILE" --jsonArray
done

echo "Importación completada."
