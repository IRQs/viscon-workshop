# Service Mesh: VIScon 2018 Workshop with ipt

This repository contains all the information and resources needed to participate in the hands-on exercise that accompanies the VIScon Workshop titled _"Service Mesh: How to tame your Microservices"_. In particular, it contains the source code of the _VIScon Order Service_ (viscon-shop-order), which is going to be part of our example microservice application called _VIScon Energy Drinks Shop_.

The README is organized into distinct steps that follow the workshop's presentation. Though we cannot stop you, you should not progress to the next step unless we tell you. Failing to do so might spoil the learning experience. If you have any questions during the exercise, please ask your friendly instructor.

### Notation
`λ`: Prompt on your _host_ machine (your notebook)

`$`: Prompt _inside_ the virtual machine

## Step 0: Prerequisites

If you want to follow the exercises on your local machine, you will need to prepare the development environment first. The exercise has been tested on Windows, but any other (sensible) operating system should work equally well (e.g. OS X, Debian, CentOS). While the actual development takes place directly on your host machine, our services will be executed within a virtual machine.

Please install the following software on your machine:
* [Oracle VM VirtualBox](https://www.virtualbox.org/wiki/Downloads) (tested with VirtualBox 5.2.18)
* [Vagrant](https://www.vagrantup.com/downloads.html) (tested with Vagrant 2.1.5)
* Any Java IDE suitable for development with Apache Maven, we recommend [IntelliJ IDEA Community](https://www.jetbrains.com/idea/download/) (tested with IntelliJ 2018.2.4)
* Windows Users (recommended): [Cmder](http://cmder.net/) is a portable console emulator for Windows. It makes working with the command line much more comfortable.
* REST API Client (recommended): [Postman](https://www.getpostman.com/apps) is an API toolkit to test your APIs. Requires a Google Account to login.


## Step 1: Setting up shop

In order to run your own instance of the _VIScon Order Service_, you need to setup your very own node (virtual machine). This node will later join the cluster of nodes hosting the other microservices that are being called by your _Order Service_.

Before your boot up your node, think of an original name for your node. If you don't feel creative, the [Pet Name Generator](https://yourpetname.com/) is your friend. Afterwards, open the file `Vagrantfile` in the root directory of this repository and edit the following line:
```
node.vm.hostname = "viscon-<your node name in lowercase here>"
```

Example (please **do not** use this name!):
```
node.vm.hostname = "viscon-master"
```


### Boot up the virtual machine
Now it's time to get you up and running. Open a shell (command line) and navigate to the root directory of the _viscon-workshop_ repository. Execute the following commands to boot up your virtual machine:

```
λ vagrant plugin install vagrant-vbguest
λ vagrant up
```

This might take a while, so take a break while listening to the next part of the presentation.

## Step 2: One network to rule them all
Once your virtual machine is ready, you can use the command `vagrant ssh` to log in using a _Secure Shell_ (SSH). At this point, your node is all by itself, living peacefully in its own network. But being alone is no fun, so let's join a virtual network. This enables your node to participate in our service mesh cluster later.

From within the virtual machine (bash), execute the following command:

```
$ sudo zerotier-cli join <network id provided by instructor>
```

When your node first joins the virtual network, it has to be authorized. Unfortunately, this is a manual process that has to be done by the instructor. That's why you should wait for further instructions before proceeding the exercise.

After you (successfully) joined the network, you should be assigned a new IP address that starts with `172.26.*`. You can identify your IP address with the command `ip addr`. Write down your 172.26.x.x IP address, you'll need it later. It should be the last entry. If there is no 172.26.x.x IP address, you might need to wait a bit and try again.

## Step 3: Let's talk shop

From within your node (VM), you can access the project folder by navigating to `cd /viscon/viscon-shop-order`. This works because the `Vagrantfile` configuration tells Vagrant to automagically mount the entire directory (stored on your host machine) from where you started the virtual machine in the first place. Changes to this directory will be reflected on your host machine and vice versa.

### Compile and start the VIScon Order Service
You can now compile & start your _VIScon Order Service_ by issuing the commands below. When you compile the application for the first time, the build tool _Apache Maven_ has to download all the necessary dependencies (Java Libraries) first, so it might take a while.

```
$ mvn package
$ java -jar target/viscon-shop-order-1.0-SNAPSHOT.jar server config.yml
```

At this point, your Order Service should be up and running! You can test your service from the host machine by navigating to http://192.168.33.15:8080/info. It should return the name of the shop, which is _"Awesome Energy Drinks"_ by default.

### Exploring the Order Service
Start your favorite Java IDE and open the root project file `pom.xml`. Navigate to the project subfolder `viscon-shop-order`, open the file `config.yml` and edit the shop name to something unique and creative. Recompile & restart the service (from within the VM) to see the new shop name when you call http://192.168.33.15:8080/info again.

### Calling the Catalogue Service
When your Order Service processes a new order, it is going to call the _VIScon Catalogue Service_ to retrieve all available energy drinks. Let's retrieve this list manually from your node to check your connectivity:

```
$ curl -s http://<viscon-services-ip>:9595/catalogue | jq
```

You should receive a list of energy drinks and their prices.

### Calling the Order Service
Your _VIScon Order Service_ has a REST API to place new orders for energy drinks. That API only has one operation:
```
HTTP POST to /order

Example payload (JSON):
{
	"user": "Karl",
	"items": [
		{
			"itemId": 1,
			"count": 3
		},
		{
			"itemId": 2,
			"count": 5
		},
		{
			"itemId": 3,
			"count": 3
		}
	]
}
```

However, before you go ahead and call that operation, you need to edit the `config.yml` file and change the IP addresses of the Catalogue and Payment service. The instructor will let you know the current IP addresses to be used in this exercise.

Now you're ready to call your _VIScon Order Service_. First, make sure that have recompiled & restarted your service. It should run on port 8080. Then, in another shell on your node, navigate to the project folder and execute the following command:
```
$ cd /viscon/viscon-shop-order

# Place a new order
$ curl -s -H "Content-Type: application/json" -d @example-order.json http://localhost:8080/order | jq

# Example response
{
  "processed": true,
  "amount": 40.5
}
```

You can edit the `example-order.json` file and change your order. Please note that if you change the `name` to anything else than `Karl`, your order will be declined by the Payment Service. The `itemId` ranges from 1 to 10. Of course, you can also use Postman and call the Order Service from your host machine using the URL http://192.168.33.15:8080/order.

## Step 4: Service Discovery

Finally, we are ready to get to know Consul, our Service Mesh solution of choice. From [consul.io](https://consul.io):
> Consul is a distributed service mesh to connect, secure, and configure services across any runtime platform and public or private cloud.

### Join the Consul cluster
Before we can query Consul and inquire about services, we have to start a local Consul agent instance and tell it to join the Consul cluster. Please note that the example responses below will differ from your actual node. For your convenience, run the Consul process in a new terminal window.

```
# Copy the Consul agent configuration to Consul's configuration directory
$ sudo cp /viscon/consul/consul-config.json /etc/consul.d/

# Start the Consul Agent in the background. You also need to advertise your 172.26.x.x IP address, so other Consul nodes can find you
$ cd ~
$ consul agent -data-dir=/tmp/consul -config-dir=/etc/consul.d -advertise <your IP address> &

# Check that you have successfully started Consul
$ consul members
Node             Address             Status  Type    Build  Protocol  DC   Segment
viscon-services  172.26.25.229:8301  alive   client  1.2.3  2         dc1  <default>

# Join the Consul cluster, you'll need the IP address of the viscon-master node (provided by instructor)
$ consul join <viscon-master-ip>
Successfully joined cluster by contacting 1 nodes.

# Check that you have successfully joined the Consul cluster
$ consul members
Node             Address             Status  Type    Build  Protocol  DC   Segment
viscon-master    172.26.114.45:8301  alive   server  1.2.3  2         dc1  <all>
viscon-services  172.26.25.229:8301  alive   client  1.2.3  2         dc1  <default>
```

#### Troubleshooting
If you ever need to shutdown Consul:
```
# Show background processes
$ jobs -l
[1]+ 24313 Running                 consul agent -data-dir=/tmp/consul -advertise 172.26.114.45 &

# Kill the process (your PID might vary)
$ kill 24313
```

### Query Services
In Consul, all registered services have an name, e.g. _catalogue_ or _payment_. You can query Consul either using the DNS API or the HTTP API. For the DNS API, the DNS name for services is NAME.service.consul. The DNS deamon provided by Consul runs on port 8600.

```
# Query the Payment Service using the DNS API
$ dig @127.0.0.1 -p 8600 payment.service.consul
```

We can also use the HTTP API instead. The HTTP API runs on port 8500.
```
# Query the Catalogue Service using the HTTP API
$ curl -s http://localhost:8500/v1/health/service/catalogue?passing | jq
```

### Implement: Use Service Discovery in your VIScon Order Service
Before you implement the Service Discovery functionality, you have to edit the config file `config.yml` and set the `serviceDiscoveryEnabled` property to `true`. This setting causes your service application to instantiate a Consul Client instance and pass it to your `OrderResource` instance. In this exercise, we use the [Consul Client for Java](https://github.com/rickfast/consul-client), which is a simple client for the Consul HTTP API.

Open the Java Class `ch.ipt.viscon.order.resources.OrderResource` and locate the _TODO Step 4_ block. This is where you should now implement the Service Discovery functionality.

Refer to the library's documentation to learn how to use the Consul Client for Java. You'll need functionality from _Example 3_. The idea is to query Consul for healthy Catalogue services and call the first available one, using its associated node IP address. Beware, once you have a healthy service instance, you have to retrieve the **node address** and not the **service address**. This is due to a bug in Consul.


## Step 5: Service Segmentation

Consul Connect provides authorized service-to-service connections by deploying local proxies that are managed by Consul. Instead of calling services directly, applications can talk to a so-called _sidecar proxy_ that is deployed alongside the application itself.

### Register your VIScon Order Service with Consul
Before your Order Service can safely communicate with the Payment Service, you have to register it with Consul so that Consul Connect can manage it. Consul services can be described in service definition files (JSON or HCL Format). To register our service, first edit the file `consul/order-service.json` and change the `name` value to _either_ `order-service-front` or `order-service-back`. If you (physically) sit in the front of the audience, choose the former. If you sit in the back, choose the latter. This distinction is for demonstration purposes only and will be explained later. Copy the file to Consul's configuration directory and reload Consul:

```
# Copy service definition to Consul's configuration directory
$ sudo cp /viscon/consul/order-service.json /etc/consul.d/

# Reload Consul to load new configuration
$ consul reload
```

You can verify that your service has registered successfully by querying Consul either via the DNS or the HTTP API.

### Create local Connect proxy for Payment Service
Instead of calling the Payment Service directly, we are now going to configure a local Connect proxy that _represents_ the remote Payment Service. Again, you have to edit the configuration file `consul/order-service.json`:
```
# Add the following configuration to the file order-service.json
{
  "service": {
    "name": "order-service-front",
    ...
    "connect": {
      "proxy": {
        "config": {
          "upstreams": [{
             "destination_name": "payment",
             "local_bind_port": 9191
          }]
        }
      }
    },
    ...
  }
}

# Copy service definition to Consul's configuration directory
$ sudo cp /viscon/consul/order-service.json /etc/consul.d/

# Reload Consul to load new configuration
$ consul reload
```

Please notice the upstream values `destination_name` and `local_bind_port`. The former specifies the service name that your local Consul-managed proxy should connect to, the latter configures that proxy to listen on local port 9191.

### Implement: Call the Payment Service through the proxy
Before you implement the Service Discovery functionality, you have to edit the config file `config.yml` and set the `connectProxyEnabled` property to `true`.

Open the Java Class `ch.ipt.viscon.order.resources.OrderResource` and locate the _TODO Step 5_ block. This is where you should now call the local proxy instead of the remote Payment Service directly.

If you are able to call the Payment Service through the Consul-managed proxy, it's time to wait for the demonstration of the Consul Connect feature to see Service Segmentation in action. The demo will be shown once all participants are ready. Please be patient while your peers finish their implementation (or help them).


## Step 6: Service Configuration

Unfortunately, this step is not yet available.

## Built With

* [Dropwizard](http://www.dropwizard.io/1.3.5/docs/) - Dropwizard is a Java framework for developing ops-friendly, high-performance, RESTful web services
* [Maven](https://maven.apache.org/) - Dependency management and build tool


## Authors

* **Matthias Geel** - *Concept and Implementation* - [LinkedIn](https://www.linkedin.com/in/matthiasgeel/), [Innovation Process Technology AG](https://ipt.ch/person/matthias-geel/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
