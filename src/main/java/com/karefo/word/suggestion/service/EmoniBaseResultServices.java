package com.karefo.word.suggestion.service;

import com.karefo.word.suggestion.dao.CholService;
import com.karefo.word.suggestion.model.CholSearchImpl;
import com.karefo.word.suggestion.model.EmoniBaseImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("EmoniBaseResultServices")
@ComponentScan(basePackages = "com")
public class EmoniBaseResultServices {

    @Autowired
    CholService service;

    @Autowired
    GraphemeResults graphemeResults;

    public List<EmoniBaseImpl> getCholResult(String word){
        List<EmoniBaseImpl> result = new ArrayList<>();
        try{
            JSONObject json = service.getCholResult(word);
            //System.out.println(json.toString());
            if(json.get("list").toString().length() > 3) {
                List<CholSearchImpl> temp = (ArrayList<CholSearchImpl>) json.get("list");

                int cnt = 0;
                for (CholSearchImpl impl : temp) {
                    //System.out.println(impl.toString());
                    EmoniBaseImpl emoni = new EmoniBaseImpl();
                    emoni.setEng_word(impl.getEnwo());
                    emoni.setTam_word(impl.getTawo());
                    emoni.setPos(impl.getPos());
                    result.add(emoni);
                    cnt++;
                    if(cnt == 5)
                        break;
                }
            } else if (json.get("didyoumean").toString().length() > 3){
                String[] words = json.get("didyoumean").toString().replace("\"","").replace("[","").replace("]","").replace(", ",",").split(",");
                for(String wd : words){
                    EmoniBaseImpl emoni = new EmoniBaseImpl();
                    emoni.setEng_word("");
                    emoni.setTam_word(wd);
                    emoni.setPos("");
                    result.add(emoni);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(result.toString());
        return result;
    }


    public List<EmoniBaseImpl> getPiriporiResult(String word) {
        StringBuilder result = new StringBuilder();
        List<EmoniBaseImpl> piri_result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        URL url = null;
        try{
            String encodeWord = URLEncoder.encode(word, "UTF-8");
            url = new URL("https://services.karky.in:8006/piripori?input="+encodeWord);
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine + " ");
            in.close();
            //System.out.println(result.toString());
            jsonArray = (JSONArray) JSONValue.parse(result.toString());
            Iterator itr= jsonArray.iterator();
            while(itr.hasNext()){
                JSONObject featureJsonObj = (JSONObject)itr.next();
                //System.out.println(featureJsonObj.toString());
                if(!featureJsonObj.get("endresult").toString().equals("failure")) {
                    String translit = (String) featureJsonObj.get("translation").toString();
                    String pos = featureJsonObj.get("pos").toString().replace("[", "").replace("]", "").replace("\"", "");
                    if (translit.trim().length() > 0) {
                        EmoniBaseImpl impl = new EmoniBaseImpl();
                        impl.setTam_word(word);
                        impl.setEng_word(translit);
                        impl.setPos(pos);
                        piri_result.add(impl);
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //System.out.println(piri_result.toString());
        return piri_result;
    }
    public List<EmoniBaseImpl> getGraphemeResults(String word) {
        StringBuilder result = new StringBuilder();
        List<EmoniBaseImpl> piri_result = new ArrayList<>();

        try{
            ArrayList<String> res=  graphemeResults.graphemepattern(word);

            for(int i=0;i<res.size();i++){
                EmoniBaseImpl impl = new EmoniBaseImpl();
                impl.setTam_word(res.get(i));
                impl.setEng_word("");
                impl.setPos("");
                piri_result.add(impl);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return piri_result;
    }

    public List<EmoniBaseImpl> getEmoniResultWithConstraint(String word, String type) {
        StringBuilder result = new StringBuilder();
        List<EmoniBaseImpl> piri_result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        URL url = null;
        try{
            String encodeWord = URLEncoder.encode(word, "UTF-8");
            url = new URL("https://services.karky.in:6002/emoni?q="+encodeWord+"&t="+type+"&auth=raj@karky.in");
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine + " ");
            in.close();
            //System.out.println(result.toString());
            jsonArray = (JSONArray) JSONValue.parse(result.toString());
            int i = 0;
            Iterator itr= jsonArray.iterator();
            while(itr.hasNext()){
                JSONObject featureJsonObj = (JSONObject)itr.next();
                String rel_word = featureJsonObj.get("word").toString();
                String pos = featureJsonObj.get("wordType").toString();
                String meaning = featureJsonObj.get("wordEnglishMeaning").toString();
                if(rel_word.trim().length() > 0){
                    EmoniBaseImpl impl = new EmoniBaseImpl();
                    impl.setTam_word(rel_word);
                    impl.setEng_word(meaning);
                    impl.setPos(pos);
                    piri_result.add(impl);
                }
                i++;
                if(i == 10)
                    break;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return piri_result;
    }


    private List<EmoniBaseImpl> getEmoniResult(String word){
        List<EmoniBaseImpl> result = new ArrayList<>();
        try{
            result = getEmoniResultWithConstraint(word,"em");
            if(result.size() < 1){
                result = getEmoniResultWithConstraint(word,"mi");
                if(result.size() < 1){
                    result = getEmoniResultWithConstraint(word,"ei");
                    if(result.size() < 1){
                        result = getEmoniResultWithConstraint(word,"");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(result.toString());
        return result;
    }

    public JSONObject getAllResult(String word, int count){
        JSONObject json_result = new JSONObject();
        try{
            List<EmoniBaseImpl> cholresult = getCholResult(word);
            List<EmoniBaseImpl> piriporiresult = getPiriporiResult(word);
            List<EmoniBaseImpl> emoniresult = getEmoniResult(word);
            List<EmoniBaseImpl> graphmeresult = getGraphemeResults(word);
            if(cholresult.size() > 0 || piriporiresult.size() > 0 || emoniresult.size() > 0) {
                json_result.put("chol_res",cholresult);
                json_result.put("piripori_res",piriporiresult);
                json_result.put("phonemes_res",emoniresult);
                json_result.put("graphemes_res",graphmeresult);
                json_result.put("req_cnt",count);
            } else {
                json_result.put("chol_res","");
                json_result.put("piripori_res","");
                json_result.put("phonemes_res","");
                json_result.put("graphemes_res","");
                json_result.put("req_cnt",count);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return json_result;
    }

/*    public JSONObject getAllResult(String word){
        JSONObject json_result = new JSONObject();
        try{
            List<EmoniBaseImpl> cholresult = getCholResult(word);
            if(cholresult.size() == 0){ //if its less than 1, then it Searches in Piripori
                cholresult = getPiriporiResult(word);
                if(cholresult.size() == 0){ //if its less than 1, then it Searches in Emoni
                    cholresult = getEmoniResult(word);
                    //System.out.println(cholresult.size());
                    if(cholresult.size() == 0) { //if its less than 1, then it Searches in Emoni
                        json_result.put("chol_res","");
                        json_result.put("piripori_res","");
                        json_result.put("rhyme_res","");
                    } else {
                        json_result.put("chol_res","");
                        json_result.put("piripori_res","");
                        json_result.put("rhyme_res",cholresult);
                    }
                } else {
                    json_result.put("chol_res","");
                    json_result.put("piripori_res",cholresult);
                    json_result.put("rhyme_res","");
                }
            } else {
                json_result.put("chol_res",cholresult);
                json_result.put("piripori_res","");
                json_result.put("rhyme_res","");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(json_result.toString());
        return json_result;
    }*/

    public static void main(String args[]){
        ConfigurableApplicationContext configurableApplicationContext= SpringApplication.run(EmoniBaseResultServices.class, args);
        EmoniBaseResultServices aadugalamService=configurableApplicationContext.getBean(EmoniBaseResultServices.class);
        aadugalamService.getAllResult("கல்லாமை",1);
    }

}
