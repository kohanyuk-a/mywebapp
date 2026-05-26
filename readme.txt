====================================
lab 4:







======================================
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

