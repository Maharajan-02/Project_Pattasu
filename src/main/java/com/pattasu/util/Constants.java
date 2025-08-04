package com.pattasu.util;

public class Constants {
	
	public static String UPLOAD_PATH;
	
	public static final String file = "file";
	public static final String fileName = "fileName";
	public static final String folder = "folder";
	public static final String upload = "/upload";
	public static final String url = "url";
	public static final String transformation = "transformation";
	public static final String transformationValue = "w-384,h-384,c-force";
	
	public static void setUploadPath(String path) {
        UPLOAD_PATH = path;
    }
	
	private Constants() {
	}
}
