# storm-samples
Small storm projects with different functionality

# requirements
All services used required to be installed outside of this project, kafka, elasticsearch,...

# samples available

## TestTopology 

Read lines from a file and adds "Bolted" to the text and prints it out to log file.
execute it as :

mvn exec:java -Dexec.mainClass="topology.TestTopology" -Dexec.args="src/main/resources/test.txt"

## TestKafkaTopology

Subscribe to "test" topic in kafka and prints the message to the log file.
execute it as :

mvn exec:java -Dexec.mainClass="topology.TestKafkaTopology" 

## TestElasticSearchTopology

Read lines from a file (expects json in every line) and adds that json to "test/bolt" index in elasticsearch.
execute it as :

mvn exec:java -Dexec.mainClass="topology.TestElasticSearchTopology" -Dexec.args="src/main/resources/json.txt" 

 