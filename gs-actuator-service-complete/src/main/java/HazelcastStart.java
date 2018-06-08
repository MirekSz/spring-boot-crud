import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapAttributeConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import com.hazelcast.query.Predicates;

import hello.repo.Product;

public class HazelcastStart {
	static ObjectMapper ob = new ObjectMapper();

	public static void main(final String[] args) {
		Config config = new Config();

		SerializerConfig sc = new SerializerConfig().setImplementation(new StreamSerializer<Product>() {

			@Override
			public int getTypeId() {
				return 10;
			}

			@Override
			public void destroy() {
				// TODO Auto-generated method stub

			}

			@Override
			public void write(final ObjectDataOutput out, final Product object) throws IOException {
				ob.writeValue(out, object);

			}

			@Override
			public Product read(final ObjectDataInput in) throws IOException {
				return ob.readValue(in, Product.class);
			}
		}).setTypeClass(Product.class);
		config.getSerializationConfig().addSerializerConfig(sc);

		MapAttributeConfig mapAttributeConfig = new MapAttributeConfig();
		mapAttributeConfig.setName("jsonprop");
		mapAttributeConfig.setExtractor("JSONValExtractor");

		config.getGroupConfig().setName("altkom").setPassword("altkomPaSS");
		config.addMapConfig(new MapConfig("default").setMaxSizeConfig(new MaxSizeConfig(1000, MaxSizePolicy.PER_NODE))
				.setEvictionPolicy(EvictionPolicy.LRU).setReadBackupData(true).setBackupCount(0).setAsyncBackupCount(1)
				.setMapAttributeConfigs(Lists.newArrayList(mapAttributeConfig)));
		HazelcastInstance newHazelcastInstance = Hazelcast.newHazelcastInstance(config);
		IMap<Object, Object> map = newHazelcastInstance.getMap("current-operations-region");

		for (int i = 0; i < 100; i++) {
			Product value = new Product();
			value.setName("asd " + i);
			map.put(i, value);

		}
		long ownedEntryCount = map.getLocalMapStats().getOwnedEntryCount();
		long back = map.getLocalMapStats().getBackupEntryCount();
		System.out.println(ownedEntryCount + " " + back);
		Collection<Object> values = map.values(Predicates.like("jsonprop", "%0"));
		for (Object object : values) {
			Product p = (Product) object;
			System.out.println(p.getName());
		}
	}

}
