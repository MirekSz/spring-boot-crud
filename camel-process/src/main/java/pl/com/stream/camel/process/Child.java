
package pl.com.stream.camel.process;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Child {

	@JacksonXmlProperty(isAttribute = true)
	private Long age;
	private final Boolean fullAge = false;

	public Child(final long age) {
		this.age = age;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(final Long age) {
		this.age = age;
	}

	public Boolean getFullAge() {
		return fullAge;
	}
}
