
#!/bin/bash

# Install required packages
sudo yum install -y -q maven bind-utils wget unzip socat nc dnsmasq

# Install jq (pretty print json)
sudo yum install -y -q epel-release
sudo yum install -y -q jq

# Install Consul binary
wget -q https://releases.hashicorp.com/consul/1.2.3/consul_1.2.3_linux_amd64.zip
unzip consul_1.2.3_linux_amd64.zip
rm consul_1.2.3_linux_amd64.zip
sudo mv consul /usr/local/bin/

# Consul Setup
sudo mkdir /etc/consul.d

# Install ZeroTier service
curl -s https://install.zerotier.com/ | sudo bash

# Configure dnsmasq to point to Consul
# cat <<EOF | sudo tee /etc/dnsmasq.d/10-consul
# # Enable forward lookup of the 'consul' domain:
# server=/consul/127.0.0.1#8600
# EOF

# Restart dnsmasq and enable service
#sudo systemctl restart dnsmasq
#sudo systemctl enable dnsmasq

# We're done
echo "Your VIScon node is ready!"
echo "Use 'vagrant ssh' to log into your node."
