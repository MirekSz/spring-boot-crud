package hello;

import javax.validation.constraints.NotNull;

public class Greeting {

	private long id;
	@NotNull
	private String content;

	public Greeting() {
	}

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

}
