package topology;

import spout.TestReadFileSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import bolt.TestSimpleBolt;
import bolt.TestWriteOutputBolt;

public class TestTopology {

	public static void main(String[] args) {

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("test", new TestReadFileSpout());
		builder.setBolt("bolted", new TestSimpleBolt()).shuffleGrouping("test");
		builder.setBolt("Output", new TestWriteOutputBolt()).shuffleGrouping("bolted");
		Config conf = new Config();
		conf.put("fileName", args[0]);
		conf.setDebug(true);
		
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("testTopology", conf, builder.createTopology());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// Do nothing
		}
		cluster.shutdown();
	}

}
