package hello.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepo extends JpaRepository<Auction, Long> {
	@Query("FROM Auction a WHERE a.creator = ?#{principal.getUsername()} OR a.creator = ?#{@company.getCompany()}")
	List<Auction> getAllForCurrentUser();

	@Override
	// @Yours
	Auction findOne(Long id);

	List<Auction> findTop10ByActiveIsTrue();
}
