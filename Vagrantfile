# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "grails-mongo-jodatime"
  config.vm.box_url = "http://files.vagrantup.com/precise64.box"
  config.vm.provider "virtualbox" do |v|
    v.customize ["modifyvm", :id, "--memory", 2048]
  end

  config.vm.synced_folder ".", "/home/vagrant/src/plugin"

  config.vm.define :db do |db|
    db.vm.hostname = "db.dev.gmj.local"
    db.vm.network :private_network, ip: "192.168.121.101"
    db.vm.provision :puppet do |puppet|
      puppet.manifests_path = "puppet/manifests"
      puppet.module_path    = "puppet/modules"
      puppet.manifest_file = "db.pp"
      puppet.facter = {
        "host_environment" => "Vagrant"
      }
      puppet.options = "--verbose"
    end
  end
end