package com.bubblewrap.plants.webserver.service;

import org.springframework.context.annotation.Bean;

public interface GifService {

	@Bean
	String getLatestGifPath();
}
