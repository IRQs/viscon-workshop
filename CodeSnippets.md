Code Snippts
============

## Configure Consul (viscon-master)
```
sudo cp /viscon/consul/consul-server-config.hcl /etc/consul.d/
sudo cp /viscon/consul/catalogue-service.json /etc/consul.d/

consul reload
```

## Configure Consul (viscon-services)
```
sudo cp /viscon/consul/consul-server-config.json /etc/consul.d/

sudo cp /viscon/consul/catalogue-service.json /etc/consul.d/
sudo cp /viscon/consul/payment-service.json /etc/consul.d/

consul reload
```

## Start Consul in Server Mode (viscon-master)
```
nohup consul agent -server -bootstrap-expect=1 -data-dir=/tmp/consul -enable-script-checks=true -config-dir=/etc/consul.d -ui -client="192.168.33.15 127.0.0.1" -advertise 172.26.56.180 &
```

## Start Consul in Agent Mode (viscon-services)
```
nohup consul agent -data-dir=/tmp/consul -config-dir=/etc/consul.d -advertise 172.26.168.232 -join 172.26.56.180 &
```

## Run Vagrant provisioning again
```
vagrant provision
```

## Start services
```
java -jar target/viscon-shop-catalogue-1.0-SNAPSHOT.jar server config.yml
java -jar target/viscon-shop-payment-1.0-SNAPSHOT.jar server config.yml
```
