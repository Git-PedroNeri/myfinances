package com.pneri.myfinances;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

	@Value("${application.name}")
	private String appName;

	public String getAppName() {
		return appName;
	}

	public void printNameApp() {
		System.out.println("${application.name}");
	}

}
