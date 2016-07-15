/**
 * Testing CountryController:
 *  - com.das.repository.CountryRepository.java 
 */

package com.das.junit;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.das.SecureApiRestApplication;
import com.das.controller.CountryController;
import com.das.model.Country;

@EnableJpaRepositories("com.das.repository")
@TestPropertySource(properties = { "spring.jmx.enabled:true", "spring.datasource.jmx-enabled:true" })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecureApiRestApplication.class)
@WebAppConfiguration
public class SecureApiRestCountryControllerTest {

	private static final int RESULTS_SIZE = 20;

	@Autowired
	private CountryController controller;

	@Autowired
	private WebApplicationContext context;	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCountryController_findAll() {
		ResponseEntity<Resources<Resource<Country>>> response = controller.findAll(0);
		List<Link> links = response.getBody().getLinks();
		assertNotNull(links);
		Collection<Resource<Country>> resources = response.getBody().getContent();
		assertEquals(String.format("The size of the list of countries must be %d", RESULTS_SIZE), resources.size(), 20);
		for (Iterator iterator = resources.iterator(); iterator.hasNext();) {
			System.out.println(iterator.next());
			Resource<Country> rsCountry = (Resource<Country>) iterator.next();
			Country c = rsCountry.getContent();
			List myList = new ArrayList();
			String info = String.format("CountryName= %s || CountryAbbreviation = %s", c.getName(), c.getAbbr());
			assertThat(info, c.getName().length(), is(not(0)));
			assertThat(info, c.getAbbr().length(), is(not(0)));
		}
		// Check if links exists:
		assertNotNull("HATEOAS Links not found", response.getBody().getLinks());		
	}
	
	@Test
	public void testCountryController_findOne() {
		ResponseEntity<Resource<Country>> response = controller.findOne(1);
		
		Country country = response.getBody().getContent();
		assertNotNull(country);		
		assertThat(country.getName().length(), is(not(0)));
		assertThat(country.getAbbr().length(), is(not(0)));
		
		// Check if links exists:
		assertNotNull("HATEOAS Links not found", response.getBody().getLinks());		
	}

	// @Test
	// public void getBadCountry() throws Exception {
	// Country country = new Country();
	// country.setId(1);
	// country.setName("test");
	//
	// Page<Country> countries = repository.findAllCustom(new PageRequest(0,
	// 20));
	// System.out.println(countries.getSize());
	//
	//
	// this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	// this.mvc.perform(get("/api/country/${id}",
	// country.getId())).andExpect(status().is4xxClientError());
	// }

}
