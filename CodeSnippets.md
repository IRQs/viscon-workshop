Code Snippts
============

## Start Consul in Server Mode (viscon-master)
```
nohup consul agent -server -bootstrap-expect=1 -data-dir=/tmp/consul -enable-script-checks=true -config-dir=/etc/consul.d -ui -client="192.168.33.15 127.0.0.1" -advertise <zerotier ip> &
```

## Start Consul in Agent Mode (viscon-services)
```
nohup consul agent -data-dir=/tmp/consul -config-dir=/etc/consul.d -advertise <zerotier ip> &
```

## Configure Consul (viscon-master)
```
sudo cp /viscon/consul/basic_consul_server_config.hcl /etc/consul.d/
sudo cp /viscon/consul/catalogue-service.json /etc/consul.d/

consul reload
```

## Configure Consul (viscon-services)
```
sudo cp /viscon/consul/*.json /etc/consul.d/

consul reload
```
