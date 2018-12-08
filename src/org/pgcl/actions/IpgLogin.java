package org.pgcl.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IpgLogin extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String password;

	private static final Logger logger = LogManager.getLogger(IpgLogin.class
			.getName());

	public String execute() {
		if (this.isPostRequest() && userId != null && password != null) {

			setJsonResponse("success", "logged in successfully");

		}
		return null;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static Logger getLogger() {
		return logger;
	}

}
