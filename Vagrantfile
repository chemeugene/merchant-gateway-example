# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  config.vm.define "merchant" do |machine|
  	machine.vm.box = "ubuntu/xenial64"
	machine.ssh.insert_key = false
  	machine.vm.network "private_network", ip: "10.0.0.5"
  	
  	machine.vm.provider "virtualbox" do |vb|
     vb.gui = false
     vb.memory = "6144"
    end
    
    $script = <<-SCRIPT
     apt-get update
     sudo apt-get install -y python-software-properties debconf-utils
     curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
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
     docker build -t merchant/merchant:1 .
     docker-compose up -d
    SCRIPT
    
    machine.vm.provision "shell", inline: $script
  end
  
end 