package com.nomura.sample.dataservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.cloud.kubernetes.enabled=false")
class DataServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
