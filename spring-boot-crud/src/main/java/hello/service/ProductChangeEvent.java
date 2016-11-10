package hello.service;

public class ProductChangeEvent {
	private boolean changed = true;
	private String type = "PRODUCT";

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
