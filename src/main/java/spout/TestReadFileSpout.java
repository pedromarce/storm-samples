package spout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TestReadFileSpout implements IRichSpout {

	private SpoutOutputCollector collector;
	private TopologyContext context;
	private BufferedReader bufferedReader;
	private boolean completed = false; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		this.context = context;
		try {
			this.bufferedReader = new BufferedReader(new FileReader(conf.get("fileName").toString()));
		} catch (FileNotFoundException e) {
			throw new RuntimeException ("Error reading file " + conf.get("fileName"));
		}
	}

	public void close() {
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public void nextTuple() {
		
		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Nothing to do
			}
			return;
		}
		String str;
		try {
			str = this.bufferedReader.readLine();
			if (str != null) {
				this.collector.emit(new Values(str));
			} else {
				completed = true;
			}
		} catch (Exception e) {
			throw new RuntimeException("Error reading tuple : ",e);
		}
	}

	public void ack(Object msgId) {
		System.console().printf(null, "ACK: " + msgId);

	}

	public void fail(Object msgId) {
		System.console().printf(null, "FAIL: " + msgId);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("test"));
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
