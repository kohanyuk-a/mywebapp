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
