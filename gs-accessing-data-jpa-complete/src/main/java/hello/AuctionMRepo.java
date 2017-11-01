package hello;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;

@Mapper
public interface AuctionMRepo {
	Cursor<Customer> findAll();

	Customer findById(Long id);
}
