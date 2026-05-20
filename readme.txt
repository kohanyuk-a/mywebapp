
==========================================================
dockerfile
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/mywebapp-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

----------
mvn clean package

docker build -t mywebapp .

docker images
----------
nginx .conf

server {
    listen 80;

    location / {
        proxy_pass http://web:8080;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
---------
docker-compose.yml
application.yml


---------------
to Run
mvn clean package
docker compose up --build

----------------
sudo rm /etc/nginx/sites-enabled/default
sudo rm /etc/nginx/sites-enabled/default

sudo cp deploy/nginx/mywebapp.conf \
/etc/nginx/sites-available/mywebapp

sudo ln -s \
/etc/nginx/sites-available/mywebapp \
/etc/nginx/sites-enabled/mywebapp

ls -la /etc/nginx/sites-enabled/

sudo nginx -t

sudo systemctl restart nginx

sudo systemctl restart docker

docker compose down

docker compose up --build

curl http://localhost -- show result in command line
==========================================================
systemctl status nginx

ss -tulpn | grep :80

------------------------
Встановлення PostgreSQL
sudo apt install postgresql postgresql-contrib -y

Перевірка статусу
sudo systemctl status postgresql

Зайти в PostgreSQL
sudo -u postgres psql

Створити користувача
CREATE USER mywebapp WITH PASSWORD 'mywebapp';

Створити БД
CREATE DATABASE mywebapp OWNER mywebapp;

Дати права
GRANT ALL PRIVILEGES ON DATABASE mywebapp TO mywebapp;

Вийти
\q

Перевірити підключення
psql -h 127.0.0.1 -U mywebapp -d mywebapp

Пароль:
mywebapp

Міграція ....

sudo apt update
sudo apt install nginx -y

curl -I http://localhost

