package com.demo.webscanner;

import com.demo.webscanner.controller.ScannerController;
import com.demo.webscanner.service.ScannerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(ScannerController.class)
@TestPropertySource(properties = "logging.level.org.springframework.web=DEBUG")
public class WebScannerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ScannerService scannerService;

	@MockBean
	private ThreadPoolTaskExecutor taskExecutor;

	@Test
	public void rendersForm() throws Exception {
		mockMvc.perform(get("/scanner"))
				.andExpect(content().string(containsString("Web Scanner")));
	}

	@Test
	public void submitsForm() throws Exception {
		mockMvc.perform(post("/scanner").param("url", "https://www.webscantest.com/").param("lookingFor", "test"))
				.andExpect(content().string(containsString("Result")));
	}

}
