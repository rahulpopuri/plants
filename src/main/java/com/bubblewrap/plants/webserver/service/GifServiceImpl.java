package com.bubblewrap.plants.webserver.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GifServiceImpl implements GifService {

	@Value("${gif.directory}")
	private String gifDirectory;
	
	@Override
	public String getLatestGifPath() {
		File latestGifFile = null;
		int maxNum = 0;
		File folder = new File(gifDirectory);
		File [] fileList = folder.listFiles();
		for(int i = 0; i < fileList.length; i++){
			if(fileList[i].isFile()){
				int num = getNumFromFilename(fileList[i].getName());
				if(maxNum < num){
					maxNum = num;
					latestGifFile = fileList[i];
				}
			}
		}
		return latestGifFile!=null ? latestGifFile.getAbsolutePath() : "Error: No gifs found";
	}
	
	/**
	 * Converts yyyyMMdd.ext to yyyyMMdd
	 * @param filename
	 * @return
	 */
	private int getNumFromFilename(String filename){
		return Integer.valueOf(filename.split("\\.")[0]);
	}

}
