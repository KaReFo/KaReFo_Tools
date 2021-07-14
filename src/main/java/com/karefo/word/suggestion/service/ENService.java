package com.karefo.word.suggestion.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


@Service
public class ENService {

    private static final Logger logger = LogManager.getLogger(ENService.class);

    /**
     * To get morphological information for the given word from PiriporiService api.
     * @param word
     * @return JSONArray : List of morph information
     */
    private JSONObject geteN(String word) {
        JSONObject result = new JSONObject();
        StringBuilder jsonString = new StringBuilder();
        try {
            URL oracle = new URL("https://karky.in:9001/karefo/service/eN?n=" + word);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonString.append(inputLine);
            }
            result = (JSONObject) JSONValue.parse(jsonString.toString());
            //System.out.println("eN::::"+result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * To eN application Result
     * @param word
     * @param result
     * @return
     */
    public JSONObject geteNJSON(String word, JSONObject result){
        List<JSONObject> num = new ArrayList<JSONObject>();
        try{
            //call en Service
            JSONObject enjsonObj = geteN(word);
            JSONObject endata = new JSONObject();
            endata.put("tawo", enjsonObj.get("ta"));
            endata.put("enwo", enjsonObj.get("en"));
            endata.put("tade", enjsonObj.get("ta"));
            endata.put("ende", enjsonObj.get("en"));
            endata.put("pos", "Number");
            endata.put("ta_c_translit", "");
            endata.put("en_c_translit", "");
            endata.put("ta_Conceptual", "");
            endata.put("en_Conceptual", "");
            endata.put("department", "");
            endata.put("tavar", "");
            endata.put("envar", "");
            endata.put("image", "");
            endata.put("UWID", "");
            num.add(endata);
            result.put("lang", "NUM");
            result.put("pop", 0.0);
            result.put("plsnt",0.0);
            result.put("translit", "");
            result.put("list", num);
            result.put("didyoumean", "");
            result.put("msg", "Result From eN Service");
            result.put("audio", "");
        }catch (Exception e){
            logger.debug("[ENService][getCholResultByEn]:"+word+"\t"+e.toString());
            e.printStackTrace();
        }
        return result;
    }


}
