# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  config.vm.define "merchant" do |machine|
  	machine.vm.box = "ubuntu/xenial64"
	machine.ssh.insert_key = false
  	machine.vm.network "private_network", ip: "10.0.0.3"
  	machine.vm.synced_folder "../", "/vagrant"
  	machine.vm.network "forwarded_port", guest: 8000, host: 8000, protocol: "tcp"
  	machine.vm.network "forwarded_port", guest: 5432, host: 5432, protocol: "tcp"
  	machine.vm.network "forwarded_port", guest: 8080, host: 8080, protocol: "tcp"
  	machine.vm.network "forwarded_port", guest: 8443, host: 8443, protocol: "tcp"
  	
  	machine.vm.provider "virtualbox" do |vb|
     vb.gui = false
     vb.memory = "4096"
    end
    
    $script = <<-SCRIPT
     apt-get update
     apt-get install nodejs
     apt-get install npm
     sudo apt-get install -y python-software-properties debconf-utils
     add-apt-repository ppa:webupd8team/java
     apt-get update
     echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections
     apt-get install -y oracle-java8-installer
     apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
     apt-add-repository 'deb https://apt.dockerproject.org/repo ubuntu-xenial main'
     apt-get update
     apt-cache policy docker-engine
     apt-get install -y docker-engine
     curl -L https://github.com/docker/compose/releases/download/1.18.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
     chmod +x /usr/local/bin/docker-compose
     apt-get install nano -y
     cd /vagrant
     ./gradlew build
     docker build -f docker/Dockerfile  -t ivlev/merchant:1 .
     docker-compose up -d
    SCRIPT
    
    machine.vm.provision "shell", inline: $script
  end
  
end 