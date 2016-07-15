/**
* ServletInitializer for WAR deployment.
* 
* @author  Daniel A. S.
* @version 0.0.1
* @since   2016-07-12 
*/
package com.das;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SecureApiRestApplication.class);
	}

}
