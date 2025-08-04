package com.pattasu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadPathInitializer {

    @Value("${uploadPath}")
    public void initUploadPath(String uploadPath) {
        Constants.setUploadPath(uploadPath);
    }
}