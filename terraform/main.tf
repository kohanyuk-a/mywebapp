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

resource "virtualbox_vm" "worker" {

  name   = "worker"
  image = "./images/jammy-server-cloudimg-amd64.vdi"
  cpus   = 2
  memory = "2048 mib"

  network_adapter {
    type           = "hostonly"
    host_interface = "vboxnet0"
  }

}