package com.pattasu.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageUploadService {

    @Value("${privateKey}")
    private String privateKey;
    
    public String upload(MultipartFile file) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(privateKey, "");  // ImageKit requires privateKey as basic auth
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(Constants.file, new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        body.add(Constants.fileName, file.getOriginalFilename());
        body.add(Constants.folder, Constants.upload);
        body.add(Constants.transformation, Constants.transformationValue);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(
        		Constants.UPLOAD_PATH, request, Map.class);

        return response.getBody().get(Constants.url).toString();
    }
}


