#!/bin/bash

set -e

apt update

apt install -y openjdk-17-jdk postgresql nginx

useradd -r -s /bin/false app || true
useradd -m student || true
useradd -m teacher || true
useradd -m operator || true

echo "teacher:12345678" | chpasswd
echo "operator:12345678" | chpasswd

mkdir -p /opt/mywebapp
mkdir -p /etc/mywebapp

cp target/mywebapp-1.0.0.jar /opt/mywebapp/mywebapp.jar
cp src/main/resources/application.yml /etc/mywebapp/application.yml

sudo -u postgres psql <<EOF
CREATE USER mywebapp WITH PASSWORD 'mywebapp';
CREATE DATABASE mywebapp OWNER mywebapp;
EOF

cp deploy/systemd/mywebapp.service /etc/systemd/system/
cp deploy/systemd/mywebapp.socket /etc/systemd/system/

cp deploy/nginx/mywebapp.conf /etc/nginx/sites-available/mywebapp

ln -sf /etc/nginx/sites-available/mywebapp        /etc/nginx/sites-enabled/mywebapp

echo "15" > /home/student/gradebook

systemctl daemon-reload
systemctl enable mywebapp.service
systemctl restart mywebapp.service

nginx -t
systemctl restart nginx
