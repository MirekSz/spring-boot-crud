
package pl.com.stream.camel.process;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private String firstName;
	private List<Child> children = new ArrayList<>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public List<Child> getChildren() {
		return children;
	}

	public void setChildren(final List<Child> children) {
		this.children = children;
	}
}
