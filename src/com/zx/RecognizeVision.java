package com.zx;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RecognizeVision {
	private static final String serviceHost = "https://api.projectoxford.ai/vision/v1.0";
	
	public static void main(String[] args) {
		BufferedInputStream bin = null;
		ByteArrayOutputStream out = null;
		
		try {
			//读取图片io
			bin = new BufferedInputStream(new FileInputStream("D:\\workspace\\newfile.jpg"));
			out = new ByteArrayOutputStream();
			int bit;
			while((bit=bin.read())!=-1){
				out.write(bit);
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//图片的输入流
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		
		OCR ocr = new RecognizeVision().recognizeVision(in, "unk", false);
		
		if(ocr==null){
			System.out.println("解析失败");
		}else{
			System.out.println(ocr.isAngleDetected);
			System.out.println(ocr.language);
			System.out.println(ocr.orientation);
			System.out.println(ocr.textAngle);
			System.out.println(ocr.regions);
		}

	}
	
	private OCR recognizeVision(InputStream in, String languageCode, boolean detectOrientation){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("language", languageCode);
		params.put("detectOrientation", detectOrientation);
		
		String path = serviceHost + "/ocr";
		String uri = WebServiceRequest.getUrl(path, params);
		
		byte[] data = null;
		try {
			data = IOUtils.toByteArray(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("data", data);
		
		WebServiceRequest restCall = new WebServiceRequest("61581f6ca568446aa15f2ff7cf386824");		
		String json = null;
		try {
			json = (String) restCall.request(uri, "POST", params, "application/octet-stream", false);
			System.out.println(json);
		} catch (VisionServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OCR ocr = null;
		try{
			ocr = new Gson().fromJson(json, OCR.class);
		}catch (JsonParseException e){
			//解析失败
		}
		
		return ocr;
	}

}
