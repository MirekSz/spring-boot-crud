package hello.repo;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hello.model.SaleDocument;

@Repository
public interface SaleDocumentRepo extends JpaRepository<SaleDocument, Long> {

	@Override
	@EntityGraph("full")
	List<SaleDocument> findAll();

	@EntityGraph(attributePaths = "items.product")
	List<SaleDocument> findTop10ByOrderByNumberDesc();
}
