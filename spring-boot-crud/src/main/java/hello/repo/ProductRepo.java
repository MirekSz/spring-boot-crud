package hello.repo;

import hello.model.Product;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
	// @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:name, '%')) ORDER BY p.name ASC")
	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(:#{'%'+#name+'%'}) ORDER BY p.name ASC")
	List<Product> getByName(@Param("name") String name);

	@Query("SELECT p FROM Product p WHERE p.name = :name")
	Stream<Product> findAllByName(@Param("name") String name);
}
