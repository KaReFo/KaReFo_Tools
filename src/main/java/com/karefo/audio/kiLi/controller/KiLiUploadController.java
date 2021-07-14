package com.karefo.audio.kiLi.controller;

import com.karefo.audio.kiLi.service.KiLiUploadService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@RestController
@CrossOrigin(allowedHeaders = "ACCESS_TOKEN,TIME_OF_CALL", origins = "*")
public class KiLiUploadController {

    @Autowired
    KiLiUploadService service;

    @RequestMapping(value ="chol/save/audio", method = RequestMethod.POST , produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"})
    public JSONObject saveCholAudio(@RequestBody String rawjasontext) {
        try{
            rawjasontext  = URLDecoder.decode(rawjasontext.toString().trim(),"UTF-8");
            rawjasontext  = rawjasontext.substring(0, rawjasontext.length());
            //System.out.println(">>|"+rawjasontext);
            return  service.saveAudioText(rawjasontext.substring(rawjasontext.indexOf("{"), rawjasontext.lastIndexOf("}")));
            //return  image.saveImageText(data.substring(data.indexOf("{"), data.lastIndexOf("}")));
        }catch (Exception e){
            e.printStackTrace();
            return new JSONObject();
        }
    }
}
