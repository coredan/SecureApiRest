/**
 * Testing Controller (Rest Api) calls:
 *  - com.das.controller.CountryController.java 
 */

package com.das.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.das.SecureApiRestApplication;
import com.das.repository.CountryRepository;

@EnableJpaRepositories("com.das.repository")
@TestPropertySource(properties = { "spring.jmx.enabled:true",
        "spring.datasource.jmx-enabled:true", "classpath:application.properties" })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecureApiRestApplication.class)
@WebAppConfiguration
public class SecureApiRestServiceTests {
		
	private static final int API_VERSION = 1;
	
	private String get_countryByIdUrl;

    @Autowired
    private WebApplicationContext context;
    
   @Value("${security.user.name}")
   private String userName;
   
   @Value("${security.user.password}")
   private String userPass;
    
    private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    private MockMvc mvc;

    @Before
    public void setUp() {
    	this.get_countryByIdUrl = String.format("/api/v%d/country/", API_VERSION);
    	this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }
    
    @Test    
    public void getCountryTest()  throws Exception  {    	    	    
        this.mvc.perform(get(get_countryByIdUrl, 1)).andExpect(status().is2xxSuccessful());    	
    }
    
    @Test
    public void userAuthenticates() throws Exception {   
    	
        ResultMatcher matcher = new ResultMatcher() {
            public void match(MvcResult mvcResult) throws Exception {
                HttpSession session = mvcResult.getRequest().getSession();                
                SecurityContext securityContext = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);                
                //Assert.assertEquals(securityContext.getAuthentication().getName(), userName);
            }
        };                      
        this.mvc.perform(post("/j_spring_security_check").param("j_username", userName).param("j_password", userPass))
                //.andExpect(redirectedUrl("/"))
                .andExpect(matcher);
    }

}
