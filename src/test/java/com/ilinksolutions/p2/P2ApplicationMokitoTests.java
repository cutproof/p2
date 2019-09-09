package com.ilinksolutions.p2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilinksolutions.p2.domains.UKVisaMessage;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@RunWith(SpringRunner.class)
@WebMvcTest(P2Application.class)
public class P2ApplicationMokitoTests
{
	@Autowired
	private MockMvc mvc;

	@Test
	public void contextLoads()
	{
	}

	@Test
	public void getEmployeeById() throws Exception
	{
		mvc.perform(MockMvcRequestBuilders.get("/getmsg/{id}", 1).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
	}

	@Test
	public void createEmployeeAPI() throws Exception
	{
		mvc.perform(MockMvcRequestBuilders.post("/savemsg")
				.content(asJsonString(new UKVisaMessage(100, "Harjeet", "Parmar", "202-277-0788", "harjeet.parmar@ilinksolutions.com")))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(100));
	}

	public static String asJsonString(final Object obj)
	{
		try
		{
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}