
package pl.com.stream.camel.process;

import java.util.List;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.dataformat.xml.ser.XmlBeanSerializerBase;
import com.fasterxml.jackson.dataformat.xml.ser.XmlBeanSerializerModifier;
import com.fasterxml.jackson.dataformat.xml.util.AnnotationUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlInfo;

public class MyCustomSerializer extends XmlBeanSerializerModifier {

	@Override
	public List<BeanPropertyWriter> changeProperties(final SerializationConfig config, final BeanDescription beanDesc,
			final List<BeanPropertyWriter> beanProperties) {
		// TODO Auto-generated method stub
		List<BeanPropertyWriter> changeProperties = super.changeProperties(config, beanDesc, beanProperties);

		final AnnotationIntrospector intr = config.getAnnotationIntrospector();

		for (int i = 0, len = beanProperties.size(); i < len; ++i) {
			BeanPropertyWriter bpw = beanProperties.get(i);
			final AnnotatedMember member = bpw.getMember();
			String ns = AnnotationUtil.findNamespaceAnnotation(intr, member);
			Boolean isAttribute = AnnotationUtil.findIsAttributeAnnotation(intr, member);
			Boolean isText = AnnotationUtil.findIsTextAnnotation(intr, member);
			Boolean isCData = AnnotationUtil.findIsCDataAnnotation(intr, member);
			bpw.setInternalSetting(XmlBeanSerializerBase.KEY_XML_INFO, new XmlInfo(true, "a", true, false));
			changeProperties.add(bpw);
		}
		return changeProperties;
	}
}
