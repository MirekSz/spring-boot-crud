package hello.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class SaleDocument {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String number;
	@OneToMany(mappedBy = "saleDocument")
	private Set<SaleDocumentItem> items = new HashSet();
	private BigDecimal totalPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Set<SaleDocumentItem> getItems() {
		return items;
	}

	public void setItems(Set<SaleDocumentItem> items) {
		this.items = items;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void addItem(SaleDocumentItem sdi) {
		if (sdi.getQuantity() == null || sdi.getQuantity() < 1) {
			throw new IllegalArgumentException();
		}
		this.getItems().add(sdi);
		sumarize();
	}

	private void sumarize() {
		this.totalPrice = items.stream()
				.map(el -> el.getProduct().getPrice().multiply(BigDecimal.valueOf(el.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

	}
}
