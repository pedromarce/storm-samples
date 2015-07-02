package topology;

import spout.TestReadFileSpout;
import spout.TestReadJDBCSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import bolt.TestSimpleBolt;
import bolt.TestWriteOutputBolt;

public class TestJDBCTopology {

	public static void main(String[] args) {

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("test", new TestReadJDBCSpout());
		builder.setBolt("Output", new TestWriteOutputBolt()).shuffleGrouping("test");
		Config conf = new Config();
		conf.setDebug(true);
		
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("testTopology", conf, builder.createTopology());
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// Do nothing
		}
		cluster.shutdown();
	}

}
