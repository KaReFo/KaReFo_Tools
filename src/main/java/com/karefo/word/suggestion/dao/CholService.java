package com.karefo.word.suggestion.dao;

import com.karefo.chollagam.model.Rank;
import com.karefo.word.suggestion.model.CholSearchImpl;
import com.karefo.word.suggestion.service.ENService;
import com.karefo.word.suggestion.service.LangIdentifierService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Service
public class CholService {

    @Autowired
    LangIdentifierService langIdentifier;

    @Autowired
    ENService enService;

    @Autowired
    CholSearchDao cholRepository;

    @Autowired
    CholVariantSearchDao variantSearch;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CholSortingAlgorithm sortingAlgorithm;

    @Autowired
    CholTranslitSearchDao translitSearch;

    @Autowired
    WordNotFoundDao wordnotfounddao;

    @Autowired
    DidYouMeanDao didYouMean;


    public List<Rank> getRankedUDIDByWord(String word){
        return jdbcTemplate.query("SELECT * FROM choldb_ver_0_1.ranked_list where word = '"+word+"'", new Object[]{}, new OnlineRankingRowMapper());
    }

    private Hashtable<Integer, CholSearchImpl> classifyByUDIDFromResult(List<CholSearchImpl> searchresult){
        Hashtable<Integer, CholSearchImpl> result = new Hashtable<>();
        try{
            for(CholSearchImpl chol : searchresult)
                result.put(chol.getUdid(), chol);
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(result.size()+"\t"+searchresult.size());
        return result;
    }

    public Rank ByteToObject(byte[] data) {
        Rank rank = new Rank();
        try {
            ByteArrayInputStream bais;
            ObjectInputStream ins;
            try {
                bais = new ByteArrayInputStream(data);
                ins = new ObjectInputStream(bais);
                rank =(Rank)ins.readObject();
                ins.close();
                bais.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return rank;
    }

    public List<CholSearchImpl> rerankBasedInIndexRanking(String word, List<CholSearchImpl> searchresultfrmdb, List<Rank> ranks){
        List<CholSearchImpl> result = new ArrayList<>();
        try{
            Hashtable<Integer, CholSearchImpl> resultfrmdb = classifyByUDIDFromResult(searchresultfrmdb);
            if(ranks != null && ranks.size() > 0) {
                //System.out.println("temp" + ranks.get(0).getWord());
                Rank rank = ByteToObject(ranks.get(0).getResult());
                //System.out.println("Object in value ::"+rank.getWord()+"\t"+rank.getUdid_list());
                //System.out.println("rank.getUdid_list().size():::::"+rank.getUdid_list().size());
                for(int x = 0; x < rank.getUdid_list().size(); x++){
                    int udid = rank.getUdid_list().get(x);
                    result.add(x, resultfrmdb.get(udid));
                    //System.out.println("::::::"+resultfrmdb.get(udid).getTawo()+"\t"+resultfrmdb.get(udid).getEnwo());
                }
                //System.out.println("rank.getUdid_list().size():::::"+result.size());

                int temp_arysize = result.size() - 1;
                if(rank.getUdid_list().size() != result.size()){
                    for (Map.Entry<Integer,CholSearchImpl> entry : resultfrmdb.entrySet()) {
                        temp_arysize+=1;
                        int udid = entry.getKey();
                        if(!rank.getUdid_list().contains(udid)){
                            result.add(temp_arysize,entry.getValue());
                            //System.out.println("::::::"+rank.getUdid_list().get(temp_arysize));
                        }
                    }
                }
            } else {
                for (Map.Entry<Integer,CholSearchImpl> entry : resultfrmdb.entrySet()) {
                    result.add(entry.getValue());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("::::::::Size:"+result.size());
        return result;
    }

    public JSONObject getCholResult(String word) {
        JSONObject result = new JSONObject();
        try {
            String lang = langIdentifier.identifyLanguage(word);
            String QueryWordOlingoa = "";
            int hwordSize = 0;
            int varwordSize = 0;
                if (!lang.equals("invalid")) {
                    if (lang.equals("num")) {
                        result = enService.geteNJSON(word, result);
                    } else if (lang.equals("ta") || lang.equals("en")) {
                        List<JSONObject> cholResult = new ArrayList<JSONObject>();
                        List<CholSearchImpl> final_result = new ArrayList<CholSearchImpl>();

                        /*Headword Search*/
                        cholResult = cholRepository.getCholResultFrmDB(word, lang);
                        hwordSize = cholResult.size();

                        /*Variantword Search*/
                        cholResult.addAll(variantSearch.searchVariantWord(word));
                        varwordSize = cholResult.size() - hwordSize;

                        if(cholResult.size() > 0) {
                            /*Merge or Combine DTID By UDID*/
                            Map<String, CholSearchImpl> mergedData = cholRepository.processSearchWord(cholResult);
                            for (Map.Entry<String, CholSearchImpl> entry : mergedData.entrySet()) {
                                final_result.add(entry.getValue());
                            }

                            /*for(CholSearchImpl obj : final_result){
                                System.out.println(obj.getTapop()+"\t"+obj.getEnpop());
                            }*/

                            /*Sorting Based on Chollagam Manual Ranking OR Popularity Score Ranking*/
                            /*Updated on 1st July 2020*/
                            List<Rank> ranks = getRankedUDIDByWord(word);
                            if(ranks != null && ranks.size() > 0){
                                final_result = rerankBasedInIndexRanking(word, final_result, ranks);
                            } else
                                final_result = sortingAlgorithm.sortData(word, final_result, lang);
                            /***Sorting***/
                            result.put("InpOli", cholRepository.getTranslit(word));
                            result.put("list", final_result);
                            result.put("didyoumean", "");
                            result.put("CompoundWord", "");
                            result.put("Limit", "");
                            result.put("InpType", lang);
                        } else { //transliteration Word Search
                            List<CholSearchImpl> translitResult = translitSearch.searchTransliteratedWord(word, lang);
                            if(translitResult.size() > 0){
                                List<JSONObject> temp = cholRepository.getActualWordByTranslit(word);
                                if(temp.size() > 0) {
                                    result.put("input", word);
                                    result.put("lang", lang);
                                    result.put("translit", temp.get(0).get("word").toString());
                                    result.put("list", translitResult);
                                    result.put("didyoumean", "");
                                    result.put("msg", "Transliteration search");
                                } else {
                                    wordnotfounddao.insertFileNotFoundWordIntoDB(word);
                                }
                            } else {     // Word Not Found
                                List<String>  tem = didYouMean.getDidYouMean(word, lang);
                                wordnotfounddao.insertFileNotFoundWordIntoDB(word);
                                result.put("input", word);
                                result.put("lang", lang);
                                result.put("pop", "0.0");
                                result.put("plsnt", "0.0");
                                result.put("translit", "");
                                result.put("list", "");
                                result.put("didyoumean", tem.size() > 0 ? tem : "");
                                result.put("msg", tem.size() > 0 ? "Did You Mean" : "Word Not Found");
                                //Kili - Audio Dictionary
                                result.put("audio", "");
                                //return result;
                            }
                        }
                    } else { // unidentified language
                        wordnotfounddao.insertFileNotFoundWordIntoDB(word);
                        result.put("input", word);
                        result.put("lang", lang);
                        result.put("pop", "0.0");
                        result.put("plsnt", "0.0");
                        result.put("translit", "");
                        result.put("list", "");
                        result.put("didyoumean", "");
                        result.put("msg", "Invalid Input / Can't identify Input Word Language 1");
                        //Kili - Audio Dictionary
                        result.put("audio", "");
                        //return result;
                    }
                } else { //invalid input text
                    result.put("msg", "Word of the Day | Input Word is Empty");
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("Final:"+word+"\t"+result.size());
        return result;
    }

}

class OnlineRankingRowMapper implements RowMapper<Rank> {
    @Override
    public Rank mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rank rank = new Rank();
        rank.setWord(rs.getString("word"));
        rank.setHeadword(Boolean.parseBoolean(rs.getString("isheadword")));
        rank.setResult(rs.getBytes("ranked_data"));
        return rank;
    }
}