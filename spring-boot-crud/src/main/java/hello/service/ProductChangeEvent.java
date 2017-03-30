package hello.service;

public class ProductChangeEvent {
	private boolean changed = true;
	private String type = "PRODUCT";
	private boolean processed;

	public void markAsProcessed() {
		this.processed = true;
	}

	public boolean isProcessed() {
		return this.processed;
	}

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
