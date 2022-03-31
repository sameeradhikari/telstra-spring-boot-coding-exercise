package com.telstra.codechallenge.first;

import java.util.Date;

public class UserAndCreatedDate {
private String login;
private Date created;
private Long followers;

public UserAndCreatedDate(String login, Date created, Long followers) {
	super();
	this.login = login;
	this.created = created;
	this.followers = followers;
}
public Long getFollowers() {
	return followers;
}
public void setFollowers(Long followers) {
	this.followers = followers;
}
public String getLogin() {
	return login;
}
public void setLogin(String login) {
	this.login = login;
}
public Date getCreated() {
	return created;
}
public void setCreated(Date created) {
	this.created = created;
}

}
