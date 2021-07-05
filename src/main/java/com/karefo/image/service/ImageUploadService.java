package com.karefo.image.service;

import com.karefo.image.dao.ImageUploadDao;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageUploadService {

    @Autowired
    ImageUploadDao dao;

    public JSONObject saveImageText(String jsonString){
        JSONObject json = new JSONObject();
        try{
            int status = 0;
            String val = jsonString.substring(jsonString.indexOf("udid=")+5,jsonString.indexOf("&")).replace(".png","").replace(".jpg","").replace(".jpeg","").trim();
            if(val!=null && val.length() > 0) {
                int udid =  Integer.parseInt(val);
                String imageText = jsonString.substring(jsonString.indexOf("&image=") + 7, jsonString.length());
                imageText = imageText.replace(" ","+");
                //System.out.println(">>>|"+imageText);
                //System.out.println("Image Text:"+imageText);//System.out.println(json.toString());
                if (dao.checkIsUWIDAvailable(udid).isEmpty()) {
                    status = dao.insertImageText(udid, imageText);
                    if (status == 1) {
                        json.put("data", "success");
                        json.put("msg", "Successfully Saved");
                    } else {
                        json.put("data", "failed");
                        json.put("msg", "Save Failed");
                    }
                } else {
                    status = dao.updateImageBase64Text(udid, imageText);
                    json.put("data", "exist");
                    json.put("msg", "Updated Successfully");
                }
            } else {
                json.put("data", "invalid");
                json.put("msg", "invalid File Name");
            }
        }catch (Exception e){
            //e.printStackTrace();
            json.put("data", "invalid");
            json.put("msg", "invalid File Name");
            return json;

        }
        return json;
    }
}
