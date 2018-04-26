
package sample.api.facebook;

import java.util.List;

import lombok.Data;

@Data
public class Feed {

	private List<Post> data;

	public List<Post> getData() {
		return data;
	}

	public void setData(final List<Post> data) {
		this.data = data;
	}

}
