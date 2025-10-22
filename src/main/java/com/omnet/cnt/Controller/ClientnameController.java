/*
 Document   : Client Name API
Author     : Sunandhaa
last update: 01/04/2024
*/

package com.omnet.cnt.Controller;


import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import com.omnet.cnt.Model.AppProperties;
import com.omnet.cnt.Service.InmateService;

@RestController
public class ClientnameController {
	
	@Autowired
	private InmateService inmateservice;
	
	private final AppProperties  appproperties;
	@Autowired
	public ClientnameController(@Qualifier("appProperties") AppProperties appProperties) {
	    this.appproperties = appProperties;
	}
      
    @GetMapping("/clientname")
    public String clientname() {
        String clientName = appproperties.getClient().getName();
        return clientName;
    }
    @GetMapping("/clientlogo")
    public ResponseEntity<byte[]> getClientLogo() {
        return getImageResponse(appproperties.getClient().getLogo(), MediaType.IMAGE_PNG);
    }

    @GetMapping("/OmnetLogo")
    public ResponseEntity<byte[]> getOmnetLogo() {
        return getImageResponse(appproperties.getOmnet().getLogo(), MediaType.IMAGE_PNG);
    }

    private ResponseEntity<byte[]> getImageResponse(String path, MediaType mediaType) {
        try {
            ClassPathResource imgFile = new ClassPathResource(path);
            byte[] bytes = Files.readAllBytes(imgFile.getFile().toPath());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } 

   
	
    @GetMapping("/inmateimage/{sbino}")
    public ResponseEntity<String> getImageBySBINO(@PathVariable String sbino, @RequestParam String userId) {
        try {
            byte[] image = inmateservice.getInmateImage(sbino, userId);

            if (image != null && image.length > 0) {
                // Convert byte array to Base64 encoded string
                String base64Image = Base64.getEncoder().encodeToString(image);
                String imageUrl = "data:image/jpeg;base64," + base64Image;
                return ResponseEntity.ok(imageUrl);
            } else {
                return ResponseEntity.ok(""); 
            }
        } catch (Exception e) {
            System.err.println("Error fetching image for sbiNo: " + sbino);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



	 
	
}

	

