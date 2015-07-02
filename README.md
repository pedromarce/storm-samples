# storm-samples
Small storm project with different functionalities in its simplest form

# Requirements
To test all the cases you will need an external instances for:
  - kafka
  - elastic search

# Samples available

## TestTopology 

Simple topology to read lines from a file and add "Bolted" to the text of every line and output final result to log file.

mvn exec:java -Dexec.mainClass="topology.TestTopology" -Dexec.args="src/main/resources/test.txt"

## TestKafkaTopology

Simple topology to subcribe to topic in kafka and output every message received to the log file.

mvn exec:java -Dexec.mainClass="topology.TestKafkaTopology" 

## TestElasticSearchTopology

Simple topology to read lines from a file representing a json object and add those json object to an index in elasticsearch

mvn exec:java -Dexec.mainClass="topology.TestElasticSearchTopology" -Dexec.args="src/main/resources/json.txt" 

 