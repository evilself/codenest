package com.codenest.demo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codenest.demo.services.UserService;
import com.codenest.model.domain.User;
import com.codenest.model.transformer.BaseTransformer;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Inject
	private UserService userService;
	
	@Inject
	@Named(value="userTransformer")
	private BaseTransformer<User, com.codenest.model.rest.User> userTransformer;
	
    @RequestMapping(method = RequestMethod.POST)
    public com.codenest.model.rest.User save(@RequestBody com.codenest.model.rest.User restModelToSave){
		User user = userService.saveUser(userTransformer.transformToDomain(restModelToSave));
        return userTransformer.transformToRest(user);
	}
    
    @RequestMapping(method = RequestMethod.GET)
    public List<com.codenest.model.rest.User> getAllUsers(){   	
    	return userService.getAllUsers().parallelStream()
							  			.map(user -> userTransformer.transformToRest(user))
							  			.collect(Collectors.toList());
	}
    
    @RequestMapping(value="/{id}" , method = RequestMethod.GET)
    public ResponseEntity<com.codenest.model.rest.User> getUserById(@PathVariable(value="id") Long id){
    	return userService.getUserByUserId(id)    			     
		    			  .map(u -> {com.codenest.model.rest.User user = userTransformer.transformToRest(u);
							return new ResponseEntity<>(user,HttpStatus.OK);
						  })
						  .orElse(ResponseEntity.notFound().build());
	}
    
    @RequestMapping(value="/{id}" , method = RequestMethod.PUT)
    public ResponseEntity<com.codenest.model.rest.User> editUserByID(@PathVariable(value="id") Long id, @RequestBody com.codenest.model.rest.User restModelToSave){
    	  return userService.getUserByUserId(id)			     
							.map(u -> {
								u.setEmail(restModelToSave.getEmail());
								u.setFirstName(restModelToSave.getFirstName());
								u.setLastName(restModelToSave.getLastName());
								u.setDateOfBirth(userTransformer.transformToDomain(restModelToSave).getDateOfBirth());
								
								u = userService.saveUser(u);				
								com.codenest.model.rest.User user = userTransformer.transformToRest(u);
								return new ResponseEntity<>(user,HttpStatus.OK);
							})
							.orElse(ResponseEntity.notFound().build());
	}
    
    @RequestMapping(value="/{id}" , method = RequestMethod.DELETE)
    public void deleteUserById(@PathVariable(value="id") Long id){		        
        userService.deleteUser(id);
	}	
}