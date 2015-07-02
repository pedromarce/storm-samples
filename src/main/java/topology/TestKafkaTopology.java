package topology;

import java.util.UUID;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import bolt.TestSimpleBolt;
import bolt.TestWriteOutputBolt;

public class TestKafkaTopology {

	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();

		BrokerHosts hosts = new ZkHosts("localhost:2181");
		SpoutConfig spoutConfig = new SpoutConfig(hosts,"test","/test",UUID.randomUUID().toString() );
		spoutConfig.scheme  = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		
		builder.setSpout("test", kafkaSpout);
		builder.setBolt("bolted", new TestSimpleBolt()).shuffleGrouping("test");
		builder.setBolt("Output", new TestWriteOutputBolt()).shuffleGrouping("bolted");
		Config conf = new Config();
		conf.setDebug(true);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("testTopology", conf, builder.createTopology());
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			// Do nothing
		}
		cluster.shutdown();
	}

}
