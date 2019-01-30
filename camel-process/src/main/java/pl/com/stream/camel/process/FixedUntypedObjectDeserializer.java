
package pl.com.stream.camel.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;

public class FixedUntypedObjectDeserializer extends UntypedObjectDeserializer {

	@Override
	@SuppressWarnings({
		"unchecked",
		"rawtypes"
	})
	protected Object mapObject(final JsonParser p, final DeserializationContext ctxt) throws IOException {
		String firstKey;

		JsonToken t = p.getCurrentToken();
		if (t == JsonToken.START_OBJECT) {
			firstKey = p.nextFieldName();
		} else if (t == JsonToken.FIELD_NAME) {
			firstKey = p.getCurrentName();
		} else {
			if (t != JsonToken.END_OBJECT) {
				throw ctxt.mappingException(handledType(), p.getCurrentToken());
			}
			firstKey = null;
		}

		// empty map might work; but caller may want to modify... so better
		// just give small modifiable
		LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>(2);
		if (firstKey == null) {
			return resultMap;
		}

		p.nextToken();
		boolean is = isAttribute(p);
		if (is) {
			System.out.println(t);
		}
		if (is) {
			Object deserialize = deserialize(p, ctxt);
			if (deserialize != null && deserialize.toString().contains("{")) {
				resultMap.put(firstKey, deserialize);
			} else {
				LinkedHashMap<String, Object> att = new LinkedHashMap<>();
				att.put(firstKey, deserialize);
				resultMap.put("attributes", att);
			}
		} else {
			resultMap.put(firstKey, deserialize(p, ctxt));
		}

		// 03-Aug-2016, jpvarandas: handle next objects and create an array
		Set<String> listKeys = new LinkedHashSet<>();

		String nextKey;
		while ((nextKey = p.nextFieldName()) != null) {
			p.nextToken();
			if (resultMap.containsKey(nextKey)) {
				Object listObject = resultMap.get(nextKey);

				if (!(listObject instanceof List)) {
					listObject = new ArrayList<>();
					((List) listObject).add(resultMap.get(nextKey));

					resultMap.put(nextKey, listObject);
				}

				((List) listObject).add(deserialize(p, ctxt));

				listKeys.add(nextKey);

			} else {
				resultMap.put(nextKey, deserialize(p, ctxt));

			}
		}

		return resultMap;

	}

	private boolean isAttribute(final JsonParser p) {
		if (2 > 1) {
			return false;
		}
		try {
			if (p instanceof FromXmlParser) {
				FromXmlParser a = (FromXmlParser) p;
				int attributeCount = a.getStaxReader().getAttributeCount();
				return (attributeCount > 0);
			}
		} catch (Exception e) {
		}
		return false;
	}

}
