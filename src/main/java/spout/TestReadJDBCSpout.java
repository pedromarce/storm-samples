package spout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.hsqldb.Server;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TestReadJDBCSpout implements IRichSpout {

	private SpoutOutputCollector collector;
	private TopologyContext context;
	private Server hsqlServer;
	private Connection connection;
	private ResultSet rs;
	private boolean completed = false; 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		this.context = context;
		this.hsqlServer = new Server();
		this.hsqlServer.setDatabaseName(0, "testDB");
		this.hsqlServer.setDatabasePath(0, "mem:testDB");
		this.hsqlServer.start();
		
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			this.connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testDB", "sa", "");
			this.connection.prepareStatement("create table test (id integer, field1 varchar(20));").execute();	
			for (int i=0;i<100;i++) {
				this.connection.prepareStatement("insert into test (id, field1) values (" + i + "," + "'field" + i + "');").execute();	
			}
			this.rs = connection.prepareStatement("select * from test;").executeQuery();
		} catch (SQLException e2) {
			//
		} catch (ClassNotFoundException e2) {
			//
		}
	}

	public void close() {
		this.hsqlServer.stop();
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
			if (this.rs.next()) {
				str = this.rs.getInt(1) + "," + this.rs.getString(2);
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
