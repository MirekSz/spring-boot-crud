package hello;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CityMapper {
	@Select("SELECT * FROM CUSTOMER WHERE id = #{id}")
	Customer findOne(@Param("id") Long id);

}
