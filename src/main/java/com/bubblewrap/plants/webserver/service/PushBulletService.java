package com.bubblewrap.plants.webserver.service;

import org.springframework.context.annotation.Bean;

public interface PushBulletService {
	
	@Bean
	void notify(String msg);

}
