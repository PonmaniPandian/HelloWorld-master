package com.example.helloworld;

import com.vaadin.Application;
import com.vaadin.data.util.sqlcontainer.SQLContainer;

/**
 * Simple login window.
 * 
 * @author Alejandro Duarte
 *
 */
public class LoginWindow extends AuthWindow {
	public LoginWindow() {
		super("Login", "Login","User", "Password", "admin", "admin");
		setClosable(false);
		getLoginTf().focus();
	}

	@Override
	public void buttonClicked() {
		SQLContainer users = AppData.getDataSource().getFreeFormQueryContainer("SELECT * FROM USER WHERE LOGIN = '" + 
							getLoginTf().getValue() + "' AND PASSWORD = '" + getPasswordTf().getValue() + "'");
		Object user = users.getIdByIndex(0);
		
		if(user != null) {
			login(getApplication(), user);
		} else {
			showError();
		}
	}

	private void login(Application application, Object user) {
		application.setUser(user);
		application.init();
	}

	private void showError() {
		label.setCaption("Invalid login or password");
		panel.setVisible(true);
	}
	
}
