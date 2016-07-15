/**
* Country Entity from simple POJO class
* 
* @author  Daniel A. S.
* @version 0.0.1
* @since   2016-07-11 
*/
package com.das.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity bean with JPA annotations
 * Hibernate provides JPA implementation
 * @author Dan
 *
 */
@Entity
@Table(name="tbl_country")
public class Country {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String abbr;

	public Country(){}
	
	public Country(String countryName, String abbreviation){
		this.name = countryName;
		this.abbr = abbreviation;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String key) {
		this.abbr = key;
	}

	@Override
	public String toString(){
		return "id="+id+", name="+name;
	}
}