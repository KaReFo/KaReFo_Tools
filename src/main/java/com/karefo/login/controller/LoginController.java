package com.karefo.login.controller;

import com.karefo.audio.kiLi.service.KiLiUploadService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@CrossOrigin(allowedHeaders = "ACCESS_TOKEN,TIME_OF_CALL", origins = "*")
public class LoginController {

    @RequestMapping(value ="karefo/tools/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method= RequestMethod.GET)
    @ResponseBody
    public int searchWordfrmCholDB(@RequestParam("uid") String uid, @RequestParam("pcode") String auth) throws UnsupportedEncodingException {
        return uid.equals("karefotools") && auth.equals("tools@13579") ? 1 : 0;
    }

}
