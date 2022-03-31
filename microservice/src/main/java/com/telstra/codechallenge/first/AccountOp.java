package com.telstra.codechallenge.first;

public class AccountOp {
	private Long id;
	private String login;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getHtml_url() {
		return html_url;
	}
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	private String html_url;
	public AccountOp(Long id, String login, String html_url) {
		super();
		this.id = id;
		this.login = login;
		this.html_url = html_url;
	}
}
