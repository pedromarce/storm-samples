package topology;

import org.elasticsearch.storm.EsBolt;

import spout.TestReadFileSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class TestElasticSearchTopology {

	public static void main(String[] args) {

		Config esConf = new Config();
		esConf.put("es.input.json", "true");
		esConf.put("es.nodes", "rhbilling01");
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("test", new TestReadFileSpout());
		builder.setBolt("es-bolt", new EsBolt("test/bolt", esConf)).shuffleGrouping("test");
		Config conf = new Config();
		conf.put("fileName", args[0]);
		conf.setDebug(true);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("testTopology", conf, builder.createTopology());
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// Do nothing
		}
		cluster.shutdown();
	}

}
