package com.mario.storm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import twitter4j.GeoLocation;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Bolt filters out a predefined set of words.
 * @author mario
 */
public class IgnoreWordsBolt extends BaseRichBolt {

	private final Set<String> IGNORE_LIST = new HashSet<String>(Arrays.asList(new String[] { "http", "the", "you",
			"que", "and", "for", "that", "like", "have", "this", "just", "with", "all", "get", "about", "can", "was",
			"not", "your", "but", "are", "one", "what", "out", "when", "get", "lol", "now", "para", "por", "want",
			"will", "know", "good", "from", "las", "don", "people", "got", "why", "con", "time", "would", "jajaja",
			"segun", "jaja", "xD", "no", "si" }));
	private OutputCollector collector;

	public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple input) {
		String lang = (String) input.getValueByField("lang");
		String word = (String) input.getValueByField("word");
		GeoLocation loc = (GeoLocation) input.getValueByField("loc");
		if (!IGNORE_LIST.contains(word)) {
			collector.emit(new Values(lang, word, loc));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("lang", "word", "loc"));
	}

}
