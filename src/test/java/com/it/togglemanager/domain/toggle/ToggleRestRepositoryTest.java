package com.it.togglemanager.domain.toggle;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;
import com.it.togglemanager.config.security.OAuthHelper;
import com.it.togglemanager.domain.toggle.Toggle;
import com.it.togglemanager.domain.toggle.ToggleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ToggleRestRepositoryTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ToggleRepository repository;
	
	@Autowired
	OAuthHelper oauthHelper;
	
	@Before
	public void deleteAllBeforeTests() throws Exception {
		this.repository.deleteAll();
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldReturnRepositoryIndex() throws Exception {

		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.toggles").exists());
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldCreateToggle() throws Exception {
		
		this.mockMvc.perform(post("/toggles")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content(new Gson().toJson(new Toggle("isButtonRed", true))))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("toggles/")));
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldRetrieveToggle() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/toggles")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content(new Gson().toJson(new Toggle("isButtonRed", true))))
			.andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		this.mockMvc.perform(get(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("isButtonRed"))
			.andExpect(jsonPath("$.value").value(true));
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldUpdateToggle() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/toggles")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content(new Gson().toJson(new Toggle("isButtonRed", true))))
			.andExpect(status().isCreated()).andReturn();;
		
		String location = mvcResult.getResponse().getHeader("Location");
		this.mockMvc.perform(put(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content(new Gson().toJson(new Toggle("isButtonYellow", true))));

		this.mockMvc.perform(get(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("isButtonYellow"))
			.andExpect(jsonPath("$.value").value(true));
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldDeleteToggle() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/toggles")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content(new Gson().toJson(new Toggle("isButtonRed", true))))
			.andExpect(status().isCreated()).andReturn();
		
		String location = mvcResult.getResponse().getHeader("Location");
		this.mockMvc.perform(get(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("isButtonRed"))
			.andExpect(jsonPath("$.value").value(true));
		
		this.mockMvc.perform(delete(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client")))
			.andExpect(status().is2xxSuccessful());
	}

}
