====================================
lab 4:
sudo apt update
sudo apt install -y ansible unzip curl wget openssh-client

sudo apt install -y gnupg software-properties-common

wget -O- https://apt.releases.hashicorp.com/gpg | \
gpg --dearmor | \
sudo tee /usr/share/keyrings/hashicorp-archive-keyring.gpg > /dev/null

echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] \
https://apt.releases.hashicorp.com $(lsb_release -cs) main" | \
sudo tee /etc/apt/sources.list.d/hashicorp.list

sudo apt update

        sudo apt install terraform -y
        OR
        sudo snap install terraform --classic

terraform version

sudo apt update
sudo apt install ansible -y

ansible --version

VBoxManage --version

sudo apt install virtualbox

mkdir -p ~/IdeaProjects/mywebapp/terraform
cd ~/IdeaProjects/mywebapp/terraform

nano main.tf

                        terraform {

                          required_providers {

                            virtualbox = {
                              source  = "terra-farm/virtualbox"
                              version = "0.2.2-alpha.1"
                            }

                          }

                        }

                        provider "virtualbox" {
                        }

terraform init


mkdir -p images
cd images

ls -lh

VBoxManage list hostonlyifs

VBoxManage hostonlyif create

VBoxManage list vms
terraform apply
VBoxManage startvm worker --type headless


file images/jammy-server-cloudimg-amd64.img
---------------
sudo apt install vagrant -y
wget https://releases.hashicorp.com/vagrant/2.4.3/vagrant_2.4.3-1_amd64.deb
sudo dpkg -i vagrant_2.4.3-1_amd64.deb

vagrant --version

nano Vagrantfile

            Vagrant.configure("2") do |config|
              config.vm.box = "ubuntu/jammy64"

              config.vm.define "worker" do |worker|
                worker.vm.hostname = "worker"
                worker.vm.network "private_network", ip: "192.168.56.10"

                worker.vm.provider "virtualbox" do |vb|
                  vb.name = "lab4-worker"
                  vb.memory = 2048
                  vb.cpus = 2
                end
              end

              config.vm.define "db" do |db|
                db.vm.hostname = "db"
                db.vm.network "private_network", ip: "192.168.56.11"

                db.vm.provider "virtualbox" do |vb|
                  vb.name = "lab4-db"
                  vb.memory = 2048
                  vb.cpus = 2
                end
              end
            end

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

