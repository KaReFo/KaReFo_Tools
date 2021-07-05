package com.karefo.audio.kiLi.service;

import com.karefo.audio.kiLi.dao.KiLiUploadDao;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KiLiUploadService {

    @Autowired
    KiLiUploadDao dao;

    public JSONObject saveAudioText(String jsonString){
        JSONObject json = new JSONObject();
        try{
            int status = 0;
            String val = jsonString.substring(jsonString.indexOf("uwid=")+5,jsonString.indexOf("&")).replace(".mp3","").trim();
            if(val!=null && val.length() > 0) {
                int udid =  Integer.parseInt(val);
                String audioText = jsonString.substring(jsonString.indexOf("&audio=data:audio/mpeg;base64,") + 30, jsonString.length());
                //System.out.println("["+uwid+" ||| "+voiceText+"]");//System.out.println(json.toString());
                if (dao.checkIsUWIDAvailable(udid).isEmpty()) {
                    status = dao.insertKiliText(udid, audioText);
                    if (status == 1) {
                        json.put("data", "success");
                        json.put("msg", "Successfully Saved");
                    } else {
                        json.put("data", "failed");
                        json.put("msg", "Save Failed");
                    }
                } else {
                    status = dao.updateKiliBase64Text(udid, audioText);
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
