/**
* The CountryController manages the countries repository.
* 	- List countries paginated by 20 elements.
* 	- Simple API versioning using response Headers and URL.
*   - HATEOAS 
*
* @author  Daniel A. S.
* @version 0.0.2
* @since   2016-07-12 
*/
package com.das.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.das.model.Country;
import com.das.repository.CountryRepository;

@RestController
@RequestMapping(value="/api/v1/country", headers="Accept=application/com.das.SecureApiRest-v1.0+json")
public class CountryController {
	public static final Long version = 1L;
	public String resHeader = null;  
	
	@Autowired
	private CountryRepository repository;
	
	public CountryController() {}
	
	/**
	 * Get a paginated list of countries (20 countries) (GET Method)
	 * @param page
	 * @return Resources containing a paginated list of countries
	 */
	@RequestMapping(method = RequestMethod.GET)	
	public ResponseEntity<Resources<Resource<Country>>> findAll(
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
		
		//Getting paginated countries from repository:
		Page<Country> countries = repository.findAllCustom(new PageRequest(page, 20));
		// Building the (HATEOAS) root Links:
		List<Link> links = new ArrayList<Link>();
		
		if (!countries.isFirst()){
			links.add(linkTo(methodOn(CountryController.class).findAll(page - 1)).withRel("prev"));
		}
		links.add(linkTo(methodOn(CountryController.class).findAll(page)).withSelfRel());
		if (!countries.isLast()) {
			links.add(linkTo(methodOn(CountryController.class).findAll(page + 1)).withRel("next"));		
		}		
		// Building the response:s
		// Change the Page Object to list of Resource.
		List<Resource<Country>> resources = countryToResource(countries.getContent().toArray(new Country[0]));
		Resources<Resource<Country>> res = new Resources<>(resources, links);
		return ResponseEntity.ok(res);
	}

	/**
	 * Adds a new country to the list (POST Method)
	 * @param country with the jSON format {"name":"Country_Name","abbr":"Country_Abbreviation"}
	 * @return Resp
	 */
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Resource<Country>> add(@RequestBody Country country) {
		List<Link> links = new ArrayList<Link>();
		links.add(linkTo(methodOn(CountryController.class).add(country)).withRel("post"));
		links.add(linkTo(methodOn(CountryController.class).findAll(1)).withRel("get"));
		links.add(linkTo(methodOn(CountryController.class).findOne(1)).withRel("get"));
		links.add(linkTo(methodOn(CountryController.class).update(1,country)).withRel("put"));
		// If the country doesn't already exist
		if (repository.findByName(country.getName()) == null) {
			// New Country object
			Country model = new Country();
			model.setName(country.getName());
			model.setAbbr(country.getAbbr());
			Country cmodel = null;
			if ((cmodel = repository.save(model)) != null){
				Resource<Country> res = new Resource<>(cmodel, links);
				return ResponseEntity.ok(res);
			} else
				new ResponseEntity<Resource<Country>>(HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<Resource<Country>>(HttpStatus.CONFLICT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Resource<Country>> findOne(@PathVariable int id) {
		List<Link> links = new ArrayList<Link>();
		links.add(linkTo(methodOn(CountryController.class).findOne(id)).withSelfRel());
		links.add(linkTo(methodOn(CountryController.class).findAll(0)).withRel("paginated"));		
		Country country = repository.findOne(id);
		if (country != null) {
			Resource<Country> countryRes = new Resource<Country>(country, links); 
			return ResponseEntity.ok(countryRes);
		}
		return new ResponseEntity<Resource<Country>>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Updates a country in the DDBB.
	 * @param id the id of the country to update
	 * @param country the new Country object (jSON)
	 * @return the updated country if no errors.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Country> update(@PathVariable int id, @RequestBody Country country) {
		Country saved = null; // used to stored the saved model
		
		// find the model to update:
		Country model = repository.findOne(id);
		if (model != null) {
			// If the new-update country name already exist
			if (repository.findByName(country.getName()) != null) {
				// returns 409 status (name conflict):
				return new ResponseEntity<Country>(HttpStatus.CONFLICT);
			}
			// updating model values:
			model.setName(country.getName());
			model.setAbbr(country.getAbbr());
			// Saving and storing the resulting country object.
			// If the result is null, returns an error:
			if ((saved = repository.save(model)) == null) {
				// returns Error 502 status (name conflict):
				new ResponseEntity<Country>(HttpStatus.BAD_GATEWAY);
			} else {
				// If no errors, returns OK 200 status and the saved country
				// object:
				return ResponseEntity.ok(saved);
			}
		}
		// returns Error 404 status:
		return new ResponseEntity<Country>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		repository.delete(id);
	}
	
	/**
	 * Converts one (or more) country objects in a list of resources.
	 * @param one (or more) Country objects (or an array of them)
	 * @return List of Resource<Country>
	 */
	private List<Resource<Country>> countryToResource(Country... countries) {

		List<Resource<Country>> resources = new ArrayList<>(countries.length);

		for (Country country : countries) {
			Link selfLink = linkTo(methodOn(CountryController.class).findOne(country.getId())).withSelfRel();
			resources.add(new Resource<Country>(country, selfLink));
		}

		return resources;
	}
}
