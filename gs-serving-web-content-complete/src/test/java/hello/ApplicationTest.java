/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class ApplicationTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ProuctRepo repo;

	@Test
	public void homePage() throws Exception {
		System.out.println(repo.count());
		Product product = new Product(new Date() + "", "a", 12, BigDecimal.TEN);
		repo.save(product);
		System.out.println(repo.count());
		mockMvc.perform(get("/index.html")).andExpect(content().string(containsString("Get your greeting")));
	}

	@Test
	public void greeting() throws Exception {
		mockMvc.perform(get("/greeting")).andExpect(content().string(containsString("Hello, World!")));
	}

	@Test
	public void greetingWithUser() throws Exception {
		mockMvc.perform(get("/greeting").param("name", "Greg"))
				.andExpect(content().string(containsString("Hello, Greg!")));
	}

}
