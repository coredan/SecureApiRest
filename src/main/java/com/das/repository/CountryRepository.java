/**
* Countries (paging and sorting) simple paged Repository interface
* @see <a href="http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/stereotype/Repository.html">@Repository</a>
* @author  Daniel A. S.
* @version 0.0.1
* @since   2016-07-11 
*/
package com.das.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.das.model.Country;


@Repository
public interface CountryRepository extends PagingAndSortingRepository<Country, Integer> {

	@Query( "select c from Country c" )
	Page<Country> findAllCustom( Pageable pageable );
	
	@Query( "select c from Country c" )
	List<Country> findAll();
	
	@Query("SELECT COUNT(c) FROM Country c")
    Integer getTotalSize();
	
	Page<Country> findAllByNameContaining( String name, Pageable pageable);
	
	Country findByName(String name);
		
}
