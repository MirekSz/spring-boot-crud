package hello;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProuctRepo extends JpaRepository<Product, Long> {

	List<Product> findByNameStartsWith(String query);

}
