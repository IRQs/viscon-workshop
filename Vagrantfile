# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  config.vm.box = "centos/7"

  config.vm.synced_folder ".", "/viscon"
  config.vm.provision "shell", path: "scripts/base_provisioning.sh"

  config.vm.define "viscon-master", primary: true do |node|
    node.vm.hostname = "viscon-master"

    # Create a private network, which allows host-only access to the machine
    # using a specific IP.
    node.vm.network "private_network", ip: "192.168.33.15"
  end

  config.vm.define "viscon-services", autostart: false do |node|
    node.vm.hostname = "viscon-services"

    node.vm.network "private_network", ip: "192.168.33.16"
  end

end
