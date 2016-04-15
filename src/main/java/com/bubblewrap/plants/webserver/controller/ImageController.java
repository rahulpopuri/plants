package com.bubblewrap.plants.webserver.controller;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bubblewrap.plants.webserver.service.GifService;

@RestController
@RequestMapping("/rest/gif")
public class ImageController {

	@Inject
	private GifService service;

	/**
	 * Returns the path to the latest gif available on the system, to be displayed on the webpage
	 * @return
	 */
	@RequestMapping("/latest")
	public ResponseEntity<String> getLatestGifPath(){
		return new ResponseEntity<String>(service.getLatestGifPath(),HttpStatus.OK);
	}
}
