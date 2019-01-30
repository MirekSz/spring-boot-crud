
package pl.com.stream.camel.process;

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.dataformat.xml.deser.WrapperHandlingDeserializer;
import com.fasterxml.jackson.dataformat.xml.deser.XmlBeanDeserializerModifier;
import com.fasterxml.jackson.dataformat.xml.deser.XmlTextDeserializer;
import com.fasterxml.jackson.dataformat.xml.util.AnnotationUtil;

public class MyCustomDerializer extends XmlBeanDeserializerModifier {

	public MyCustomDerializer() {
		super("name");
		// TODO Auto-generated constructor stub
	}

	@Override
	public BeanDeserializerBuilder updateBuilder(final DeserializationConfig config, final BeanDescription beanDesc,
			final BeanDeserializerBuilder builder) {
		// TODO Auto-generated method stub
		return super.updateBuilder(config, beanDesc, builder);
	}

	@Override
	public List<BeanPropertyDefinition> updateProperties(final DeserializationConfig config, final BeanDescription beanDesc,
			final List<BeanPropertyDefinition> propDefs) {
		// TODO Auto-generated method stub
		List<BeanPropertyDefinition> updateProperties = super.updateProperties(config, beanDesc, propDefs);
		return updateProperties;
	}

	@Override
	public JsonDeserializer<?> modifyDeserializer(final DeserializationConfig config, final BeanDescription beanDesc,
			final JsonDeserializer<?> deser0) {
		if (!(deser0 instanceof BeanDeserializerBase)) {
			// return deser0;
		}
		/*
		 * 17-Aug-2013, tatu: One important special case first: if we have one "XML Text" property, it may be exposed as VALUE_STRING token
		 * (depending on whether any attribute values are exposed): and to deserialize from that, we need special handling unless POJO has
		 * appropriate single-string creator method.
		 */
		BeanDeserializerBase deser = (BeanDeserializerBase) deser0;

		// Heuristics are bit tricky; but for now let's assume that if POJO
		// can already work with VALUE_STRING, it's ok and doesn't need extra support
		ValueInstantiator inst = deser.getValueInstantiator();
		// 03-Aug-2017, tatu: [dataformat-xml#254] suggests we also should
		// allow passing `int`/`Integer`/`long`/`Long` cases, BUT
		// unfortunately we can not simple use default handling. Would need
		// coercion.
		if (!inst.canCreateFromString()) {
			SettableBeanProperty textProp = _findSoleTextProp(config, deser.properties());
			if (textProp != null) {
				return new XmlTextDeserializer(deser, textProp);
			}
		}
		return new WrapperHandlingDeserializer(deser);
	}

	private SettableBeanProperty _findSoleTextProp(final DeserializationConfig config, final Iterator<SettableBeanProperty> propIt) {
		final AnnotationIntrospector ai = config.getAnnotationIntrospector();
		SettableBeanProperty textProp = null;
		while (propIt.hasNext()) {
			SettableBeanProperty prop = propIt.next();
			AnnotatedMember m = prop.getMember();
			if (m != null) {
				// Ok, let's use a simple check: we should have renamed it earlier so:
				PropertyName n = prop.getFullName();
				if (_cfgNameForTextValue.equals(n.getSimpleName())) {
					// should we verify we only got one?
					textProp = prop;
					continue;
				}
				// as-attribute are ok as well
				Boolean b = AnnotationUtil.findIsAttributeAnnotation(ai, m);
				if (b != null && b.booleanValue()) {
					continue;
				}
			}
			// Otherwise, it's something else; no go
			return null;
		}
		return textProp;
	}

}
