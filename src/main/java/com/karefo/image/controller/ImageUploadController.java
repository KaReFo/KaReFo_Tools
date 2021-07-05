package com.karefo.image.controller;

import com.karefo.image.service.ImageUploadService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.net.URLDecoder;

@RestController
@CrossOrigin(allowedHeaders = "ACCESS_TOKEN,TIME_OF_CALL", origins = "*")
public class ImageUploadController {

    @Autowired
    ImageUploadService service;

    @RequestMapping(value ="chol/save/image", method = RequestMethod.POST , produces = {"application/json"}, consumes = {"application/x-www-form-urlencoded"})
    public JSONObject saveCholImage(@RequestBody String rawjasontext) {
        try{
            rawjasontext  = URLDecoder.decode(rawjasontext.toString().trim(),"UTF-8");
            rawjasontext  = rawjasontext.substring(0, rawjasontext.length());
            //System.out.println(">>|"+rawjasontext);
            return  service.saveImageText(rawjasontext.substring(rawjasontext.indexOf("{"), rawjasontext.lastIndexOf("}")));
            //return  image.saveImageText(data.substring(data.indexOf("{"), data.lastIndexOf("}")));
        }catch (Exception e){
            e.printStackTrace();
            return new JSONObject();
        }
    }

}
