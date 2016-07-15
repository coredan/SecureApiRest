/**
 * Testing Repositories:
 *  - com.das.repository.CountryRepository.java 
 */

package com.das.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.das.SecureApiRestApplication;
import com.das.model.Country;
import com.das.repository.CountryRepository;

@EnableJpaRepositories("com.das.repository")
@TestPropertySource(properties = { "spring.jmx.enabled:true",
        "spring.datasource.jmx-enabled:true" })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecureApiRestApplication.class)
@WebAppConfiguration
public class SecureApiRestRepositoryTests {
	
	private static final int RESULTS_SIZE = 20; 

    @Autowired
    private CountryRepository repository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Test
    public void testFindAllCustom() {
        Page<Country> countries = repository.findAllCustom(new PageRequest(0, 20));
        //List<Country> firstList = countries.        
        // Check not null:
        assertNotNull(countries);
        // Check the correct size of the paged results:
        assertEquals(
        	String.format("The size of the list of countries must be %d",RESULTS_SIZE),
        	countries.getSize(), 20
        );
        // Check the first country: Albania.
        assertEquals(countries.getContent().get(0).getName(), "Albania");
        // Check the entire list:
        //assertEquals(countries.getContent().,"");
        //[Africa, Asia, Europe, North America, South America, Oceania, Antarctica]
    }
    
    @Test
    public void testFindOne(){
    	Country country = repository.findOne(10);
    	// Check not null:
        assertNotNull(country);
        assertEquals("[\""+country.getName()+"\",\""+ country.getAbbr()+"\"]", "[\"Australia\",\"AU\"]");
    }

}
