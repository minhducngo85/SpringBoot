package com.minhduc.tuto.springboot.bootstrapping.filehandling;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class FileUploadClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadClient.class.getTypeName());

    /**
     * to upload a Robot output file
     * 
     * @param robotFilePath
     *            the robot file path
     * @throws IOException
     */
    public void uploadRobot(String serverAddess, String robotFilePath) throws IOException {
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	FileSystemResource file = new FileSystemResource(robotFilePath);
	body.add("file", file);
	HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
	String serverUrl = serverAddess + "/rest/upload";
	RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, String.class);
	LOGGER.debug("Upload result: " + response.toString());
    }

    public static void main(String[] args) throws IOException {
	FileUploadClient uploadClient = new FileUploadClient();
	uploadClient.uploadRobot("http://localhost:8080", "/var/tmp/upload/readme.txt");
    }
}
