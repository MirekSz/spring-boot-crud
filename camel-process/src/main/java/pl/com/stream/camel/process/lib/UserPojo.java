
package pl.com.stream.camel.process.lib;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class UserPojo {

	private String name;
	private Integer age;

	@ApiModelProperty(readOnly = true)
	private Date createdDate = new Date();

	@ApiModelProperty(readOnly = true, example = "2016-01-31 18:00:00")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdLocalDate = LocalDateTime.now();

	// Getters, setters
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(final Integer age) {
		this.age = age;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getCreatedLocalDate() {
		return createdLocalDate;
	}

	public void setCreatedLocalDate(final LocalDateTime createdLocalDate) {
		this.createdLocalDate = createdLocalDate;
	}

}
