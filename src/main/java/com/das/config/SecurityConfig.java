/**
* Spring Security configuration class.
* 
* @author  Daniel A. S.
* @version 0.0.1
* @since   2016-07-13 
*/
package com.das.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
        //.antMatchers(HttpMethod.GET, "/api/**").authenticated()
        .antMatchers(HttpMethod.POST, "/api/**").authenticated()
        .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
        .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
        .anyRequest().permitAll()
        //.anyRequest().anonymous() 
        .and()
      .httpBasic().and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }
}