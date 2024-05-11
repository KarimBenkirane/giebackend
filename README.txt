Le projet utilise JDK 21, s'il existe un quelconque problème, veuillez télécharger cette version du JDK.

Le backend créé automatiquement le schema "gie" avec toutes les tables, si un schema existe déjà sous ce nom, veuillez le drop afin d'éviter des conflits.


Le javafx-sdk22.0.1 est disponible sur ce lien afin de le télécharger: https://drive.google.com/drive/folders/10xm5eI5usb9-UmPqv7MEtksoBGNV42jq?usp=sharing

Un lien Google Drive contient les projets déjà packagés avec le javafx-sdk: https://drive.google.com/drive/folders/1ix-hBbGOZVZhzyYz8WpbM-jA32v8uOVv?usp=sharing

Commande pour lancer le frontend:
java --module-path ".\javafx-sdk-22.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar .\GieFrontend1-1.0-SNAPSHOT.jar

Commande pour lancer le backend:
java -jar gie-backend-1.0-SNAPSHOT.jar