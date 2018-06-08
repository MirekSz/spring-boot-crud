import com.hazelcast.query.extractor.ValueCollector;
import com.hazelcast.query.extractor.ValueExtractor;
import com.hazelcast.query.extractor.ValueReader;

import hello.repo.Product;

public class JSONValExtractor extends ValueExtractor<Product, ValueReader> {
	@Override
	public void extract(Product arg, ValueReader valueReader, ValueCollector valueCollector) {
		valueCollector.addObject(arg.getName());
		System.out.println("dodaje " + arg.getName());

	}
}
