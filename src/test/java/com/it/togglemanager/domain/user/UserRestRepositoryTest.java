package com.it.togglemanager.domain.user;

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

import com.it.togglemanager.config.security.OAuthHelper;
import com.it.togglemanager.domain.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestRepositoryTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository repository;
	
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
			.andExpect(jsonPath("$._links.users").exists());
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldCreateUser() throws Exception {

		this.mockMvc.perform(post("/users")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content("{\"userName\": \"apiOne\", \"password\": \"apiOneSecurity\", \"version\":\"v1.1.1\", \"roles\":[\"ROLE_ADMIN\"]}"))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("users/")));
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldRetrieveUser() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/users")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content("{\"userName\": \"apiOne\", \"password\": \"apiOneSecurity\", \"version\":\"v1.1.1\", \"roles\":[\"ROLE_ADMIN\"]}"))
			.andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		this.mockMvc.perform(get(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.userName").value("apiOne"))
			.andExpect(jsonPath("$.password").value("apiOneSecurity"))
			.andExpect(jsonPath("$.version").value("v1.1.1"))
			.andExpect(jsonPath("$.roles").value("ROLE_ADMIN"));
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldUpdateUser() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/users")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content("{\"userName\": \"apiOne\", \"password\": \"apiOneSecurity\", \"version\":\"v1.1.1\", \"roles\":[\"ROLE_ADMIN\"]}"))
			.andExpect(status().isCreated()).andReturn();
		
		String location = mvcResult.getResponse().getHeader("Location");
		this.mockMvc.perform(put(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content("{\"userName\": \"apiOne\", \"password\": \"apiOneSecurity\", \"version\":\"v1.1.2\", \"roles\":[\"ROLE_ADMIN\"]}"))
			.andExpect(status().is2xxSuccessful());

	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldDeleteUser() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/users")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content("{\"userName\": \"apiOne\", \"password\": \"apiOneSecurity\", \"version\":\"v1.1.2\", \"roles\":[\"ROLE_ADMIN\"]}"))
			.andExpect(status().isCreated()).andReturn();
		
		String location = mvcResult.getResponse().getHeader("Location");
		this.mockMvc.perform(delete(location)
			.with(this.oauthHelper.bearerToken("my-trusted-client")))
			.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void shouldCreateUserWithToggle() throws Exception {

		MvcResult mvcToggleResult = this.mockMvc.perform(post("/toggles")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content("{\"name\": \"isButtonRed\", \"value\": true}"))
			.andExpect(status().isCreated()).andReturn();
		
		String toggleLocation = mvcToggleResult.getResponse().getHeader("Location");
		String user = "{\"userName\": \"apiOne\", \"password\": \"apiOneSecurity\", \"version\":\"v1.1.1\", \"roles\":[\"ROLE_ADMIN\"], \"toggles\": [\"" + toggleLocation + "\"]}";
		
		this.mockMvc.perform(post("/users")
			.with(this.oauthHelper.bearerToken("my-trusted-client"))
			.content(user))
			.andExpect(status().isCreated()).andReturn();
		
	}
	
}
