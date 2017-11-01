package hello;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class RestCustomer {
	@Autowired
	CustomerRepository cr;

	@Autowired
	CityMapper cityMapper;
	@Autowired
	AuctionMRepo auctionMRepo;
	@Autowired
	private SqlSession sqlSession;

	@GetMapping("/{id}")
	public Customer find(@PathVariable("id") Long id) {
		System.out.println(sqlSession);
		Customer customer = cityMapper.findOne(id);
		return customer;
	}
}
