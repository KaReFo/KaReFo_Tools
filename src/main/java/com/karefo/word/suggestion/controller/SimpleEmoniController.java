package com.karefo.word.suggestion.controller;

import com.karefo.word.suggestion.constant.ConstantValues;
import com.karefo.word.suggestion.service.EmoniBaseResultServices;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@CrossOrigin(allowedHeaders = "ACCESS_TOKEN,TIME_OF_CALL", origins = "*")
public class SimpleEmoniController {

    @Autowired
    EmoniBaseResultServices service;


    private boolean isValidUser(String id, String pwd){
        try{
            for(int i=0; i < ConstantValues.USER_DETAILS.length; i++){
                for(int j=0; j < ConstantValues.USER_DETAILS[i].length; j++){
                    //System.out.println(ConstantValues.USER_DETAILS[i][j]+"==="+id);
                    //System.out.println(ConstantValues.USER_DETAILS[i][j+1]+"==="+pwd);
                    if(ConstantValues.USER_DETAILS[i][j].toString().equals(id.trim()) && ConstantValues.USER_DETAILS[i][j+1].toString().equals(pwd.trim()))
                        return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    @RequestMapping(value = "karefo/tool/word_suggestion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public JSONObject getEmoniBaseResult(@RequestParam("w") String word, @RequestParam("id") String mailid, @RequestParam("psw") String passcode) throws UnsupportedEncodingException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        //System.out.println("Current:"+dtf.format(now));

        //System.out.println(">>>>>>|"+isValidUser(mailid, passcode));

        if(dtf.format(now).equals(ConstantValues.TODAYS_DATE) && ConstantValues.REQUEST_COUNT < ConstantValues.TOTAL_REQUEST_COUNT && isValidUser(mailid, passcode) == true) {
            ConstantValues.REQUEST_COUNT = ConstantValues.REQUEST_COUNT + 1;
            //System.out.println("REQUEST_COUNT:"+ConstantValues.REQUEST_COUNT);
            return service.getAllResult(word, ConstantValues.REQUEST_COUNT);
        } else if(ConstantValues.REQUEST_COUNT < ConstantValues.TOTAL_REQUEST_COUNT && isValidUser(mailid, passcode) == true){
            ConstantValues.TODAYS_DATE = dtf.format(now);
            ConstantValues.REQUEST_COUNT = 1;
            //System.out.println("REQUEST_COUNT:"+ConstantValues.REQUEST_COUNT);
            return service.getAllResult(word, ConstantValues.REQUEST_COUNT);
        }
        //System.out.println("REQUEST_COUNT:"+ConstantValues.REQUEST_COUNT);
        JSONObject json = new JSONObject();
        json.put("chol_res","limit exceed");
        json.put("piripori_res","limit exceed");
        json.put("phonemes_res","limit exceed");
        json.put("graphemes_res","");
        json.put("req_cnt","limit exceed");
        return json;
    }
}

