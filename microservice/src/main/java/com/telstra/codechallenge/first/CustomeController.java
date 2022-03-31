package com.telstra.codechallenge.first;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomeController {
	@Autowired
	CustomService customService;
	@GetMapping(value = "/getUser")
	  public Map<String,Object> zeroFollowerUser(@RequestParam Long count) throws Exception{
		  return customService.zeroFollowerUser(count);
	  }
	@GetMapping(value = "/highestStarred")
	  public Map<String,Object> getHighestStarred(@RequestParam Long count) throws Exception{
		  return customService.getHighestStarred(count);
	  }
}
