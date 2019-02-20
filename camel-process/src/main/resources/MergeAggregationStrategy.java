
package pl.com.stream.esb.config;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class MergeAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
		if (oldExchange.isFailed()) {
			return oldExchange;
		}
		transformMessage(oldExchange.getIn(), newExchange.getIn());
		return oldExchange;
	}

	private void transformMessage(final Message oldM, final Message newM) {
		oldM.setHeaders(newM.getHeaders());
	}

}
