package hello.mrepo;

import hello.repo.Auction;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuctionMRepo {
	List<Auction> findAll();

	Auction findById(Long id);
}
