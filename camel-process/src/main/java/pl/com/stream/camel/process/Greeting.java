
package pl.com.stream.camel.process;

public class Greeting {

	private long incrementAndGet;
	private String format;

	public Greeting() {

	}

	public Greeting(final long incrementAndGet, final String format) {
		this.incrementAndGet = incrementAndGet;
		this.format = format;
		// TODO Auto-generated constructor stub
	}

	public String getFormat() {
		return format;
	}

	public long getIncrementAndGet() {
		return incrementAndGet;
	}

}
