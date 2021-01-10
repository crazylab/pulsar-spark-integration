##Spike: Pulsar-Spark Integration 
The aim of this spike is to run a spark job which reads from one pulsar topic and publish into another

## Pulsar Reference Commands
List Tenants / Namespaces / Topics:
```shell script
$ bin/pulsar-admin tenants list
$ bin/pulsar-admin namespaces list public
$ bin/pulsar-admin topics list public/test-namespace
```

Create a namespace:
```shell script
$ bin/pulsar-admin namespaces create public/test-namespace
$ bin/pulsar-admin topics create public/test-namespace/test-topic
```


Produce message:
```shell script
$ bin/pulsar-client produce test-topic --messages "hello-pulsar"
```

Consume message:
```shell script
$ bin/pulsar-client consume test-topic -s "first-subscription” -n 5
```
