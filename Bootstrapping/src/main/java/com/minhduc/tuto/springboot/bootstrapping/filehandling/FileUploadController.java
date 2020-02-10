package com.minhduc.tuto.springboot.bootstrapping.filehandling;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/rest")
public class FileUploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class.getTypeName());
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
	LOGGER.debug("file name = " + file.getOriginalFilename());
	File convertFile = new File("/var/tmp/uploaded/" + file.getOriginalFilename());
	convertFile.createNewFile();
	FileOutputStream fout = new FileOutputStream(convertFile);
	fout.write(file.getBytes());
	fout.close();
	return "File is upload successfully";
    }
}
