package twitter.topics.dataStreams;

import java.util.Map;

import redis.clients.jedis.Jedis;
import twitter.topics.dataStreams.dominio.Topic;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import com.google.gson.Gson;

public class TwitterStoreHashtagsBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 4637653361653036938L;

	Jedis jedis;
	Topic topic;
	Gson gson;

	@Override
	public void cleanup() {
		//
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String hashtag = (String) input.getValueByField("hashtag");
		String hashtagFrecuency = (String) input.getValueByField("frequencyValue");
		Integer freq = new Integer(hashtagFrecuency);
		if (freq > 1) {
			topic.setHashtag(hashtag);
			topic.setFrecuency(hashtagFrecuency);
			String json = gson.toJson(topic);
			jedis.rpush("topics", json);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		jedis = new Jedis("127.0.0.1", 6379);
		topic = new Topic();
		gson = new Gson();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//
	}

}
