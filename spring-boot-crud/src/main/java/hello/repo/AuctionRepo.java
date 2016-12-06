package hello.repo;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepo extends JpaRepository<Auction, Long> {
	@Query("FROM Auction a WHERE a.creator = ?#{principal.getUsername()} OR a.creator = ?#{@company.getCompany()}")
	@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
	List<Auction> getAllForCurrentUser();

	@Override
	// @Yours
	@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
	Auction findOne(Long id);

	@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
	List<Auction> findTop10ByActiveIsTrue();
}
