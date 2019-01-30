
package pl.com.stream.camel.process;

import org.apache.commons.io.IOUtils;

import groovy.lang.GroovyShell;
import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;

// http://mrhaki.blogspot.com/2011/05/groovy-goodness-change-xml-structure.html
// http://groovy-lang.org/processing-xml.html
public class JacksonTest {

	public static void main(final String[] args) throws Exception {
		GroovyShell groovyShell = new GroovyShell();
		String xml =
				"<Person><firstName>Stefan</firstName><children><child age=\"5\"><fullAge age=\"12\">false</fullAge></child><child age=\"15\"><fullAge>false</fullAge></child></children></Person>";
		// age=\"15\"><fullAge>false</fullAge></children></children></Person>";
		GPathResult parseText = new XmlSlurper().parseText(xml);

		groovyShell.setVariable("xml", parseText);
		groovyShell.evaluate(IOUtils.toString(JacksonTest.class.getResourceAsStream("/script.groovy")));
		// JacksonXmlModule

		// SimpleModule testModule = new SimpleModule("MyModule", new Version(1, 0, 0, null)) {
		//
		// @Override
		// public void setupModule(final SetupContext context) {
		// context.addBeanSerializerModifier(new MyCustomSerializer());
		// context.addBeanDeserializerModifier(new MyCustomDerializer());
		// }
		// };
		// assuming serializer declares correct class to bind to
		// ObjectMapper objectMapper = new ObjectMapper();
		// XmlMapper xmlMapper = new XmlMapper();
		// xmlMapper.registerModule(new SimpleModule() {
		//
		// @Override
		// public void setupModule(final SetupContext context) {
		// context.addBeanSerializerModifier(new MyCustomSerializer());
		// super.setupModule(context);
		// };
		// }.addDeserializer(Object.class, new FixedUntypedObjectDeserializer()));
		// // xmlMapper.registerModule(testModule);
		// xmlMapper.setDefaultUseWrapper(true);
		// Person person = new Person();
		// person.setFirstName("Stefan");
		// person.getChildren().add(new Child(5L));
		// person.getChildren().add(new Child(15L));
		// // String personString = objectMapper.writeValueAsString(person);
		// // System.out.println(personString);
		// //
		// // String personStringXML = xmlMapper.writeValueAsString(person);
		// // System.out.println(personStringXML);
		// // Map jsonMap = objectMapper.readValue(personString, Map.class);
		// Map<String, Object> xmlMap = (Map<String, Object>) xmlMapper.readValue(
		// "<Person><firstName>Stefan</firstName><children><children age=\"5\"><fullAge age=\"12\">false</fullAge></children><children
		// age=\"15\"><fullAge>false</fullAge></children></children></Person>",
		// Object.class);
		//
		// // Map<String, Object> items = new HashMap<>();
		// // Map<String, Object> nest = new HashMap<>();
		// // nest.put("name", "a");
		// // nest.put("@name", "a");
		// // nest.put("last-name", "a");
		// // items.put("item", Arrays.asList(nest, nest));
		// // xmlMap.put("items", items);
		// System.out.println(xmlMap);
		// ObjectWriter withRootName = xmlMapper.writer().withRootName("Person");
		// String writeValueAsString = withRootName.writeValueAsString(xmlMap);
		// System.out.println(writeValueAsString);
		// // System.out.println(xmlMapper.writer().withDefaultPrettyPrinter().withRootName("abc").writeValueAsString(xmlMap));

	}
}
