package com.telstra.codechallenge.first;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomService {

	@Autowired
	private RestTemplate restTemplate;

	public Map<String, Object> zeroFollowerUser(Long count) throws Exception {

		User[] users = restTemplate.getForObject("https://api.github.com/users", User[].class);
		List<UserDetails> usersList = new ArrayList<>();
		for (User user : users) {
			UserDetails ud = restTemplate.getForObject("https://api.github.com/users/" + user.getLogin(),
					UserDetails.class);
			usersList.add(new UserDetails(user.getLogin(), ud.getFollowers(), ud.getCreated_at()));
		}
		usersList = usersList.stream().filter(x -> x.getFollowers().equals(0)).collect(Collectors.toList());
		usersList.sort((a, b) -> a.getCreated_at().compareTo(b.getCreated_at()));
		if(Integer.valueOf(0).equals(usersList.size())) {
			throw new Exception("No user with zero followers");
		}
		else if (count > usersList.size()) {
			throw new Exception("Less number of users with zero followers");
		} else {
			List<AccountOp> op = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				op.add(new AccountOp(usersList.get(i).getId(), usersList.get(i).getLogin(),
						usersList.get(i).getHtml_url()));
			}
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("response", op);
			return responseMap;
		}
	}

	public Map<String, Object> getHighestStarred(Long count) {
		// TODO Auto-generated method stub
//		html_url
//		  watchers_count
//		  language
//		  description
//		  name
//		/search/repositories
		return null;
	}
}
