package com.karefo.word.suggestion.dao;

import com.karefo.word.suggestion.model.CholSearchImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CholSearchDao {

    private static final Logger logger = LogManager.getLogger(CholSearchDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JSONtoCholConverter converter;



    /**
     * To Get Total Chol Tamil Word (Both Chol & TVU) Count In DB
     * @return total Count
     */
    public List<Integer> getCholTamilWordListSize() {
        try {
            return jdbcTemplate.queryForList("SELECT COUNT(DISTINCT word) from choldb_ver_0_1.words WHERE lang_id = 1 AND  popularity_score > 0;",  Integer.class);
        }catch (Exception e){
            logger.debug("[Chol_Repo][getCholTamilWordListSize]"+"\t"+e.toString());
            e.printStackTrace();
        }
        return new ArrayList<Integer>();
    }


    /**
     * To Get Total Chol English Word (Both Chol & TVU) Count In DB
     * @return total Count
     */
    public List<Integer> getCholEnglishWordListSize() {
        try {
            return jdbcTemplate.queryForList("SELECT COUNT(DISTINCT word) from choldb_ver_0_1.words WHERE lang_id = 10 AND  popularity_score > 0",  Integer.class);
        }catch (Exception e){
            logger.debug("[Chol_Repo][getCholEnglishWordListSize]"+"\t"+e.toString());
            e.printStackTrace();
        }
        return new ArrayList<Integer>();
    }

    /**
     * To Get Total Chol English Word (Both Chol & TVU) Count In DB for Pleasentness
     * @return total Count
     */
    public List<Integer> getCholTamilWordListSizeForPlsnt() {
        try {
            return jdbcTemplate.queryForList("SELECT COUNT(DISTINCT word) from choldb_ver_0_1.words WHERE lang_id = 1 AND  pleasantness_score > 0.0",  Integer.class);
        }catch (Exception e){
            logger.debug("[Chol_Repo][getCholTamilWordListSizeForPlsnt]"+"\t"+e.toString());
            e.printStackTrace();
        }
        return new ArrayList<Integer>();
    }

    /**
     * To Get Total Chol Word (Both Chol & TVU) Count In DB
     * @return total Count
     */
    public int getCholSize$TVU() {
        try {
            return jdbcTemplate.queryForObject("SELECT count(distinct word) as count FROM choldb_ver_0_1.words",  Integer.class);
        }catch (Exception e){
            logger.debug("[Chol_Repo][getCholSize$TVU]"+"\t"+e.toString());
            e.printStackTrace();
        }
        return 0;
    }


    /***
     * To get Popularity Score for Tamil Word from ta_popularity Table.
     * @param word : tamil word.
     * @return Popularity Score.
     */
    public int getDistinctPopularity_ta(String word) {
        try {
            List<Integer> temp =  jdbcTemplate.queryForList("select row_num from choldb_ver_0_1.ta_popularity WHERE TaWo = '"+word+"'",  Integer.class);
            if(!temp.isEmpty())
                return temp.get(0);
        }catch (Exception e){
            logger.debug("[Chol_Repo][getDistinctPopularity_ta]:"+word+"\t"+e.toString());
            e.printStackTrace();
        }
        return 0;
    }

    /***
     * To get Popularity Score for English Word from en_popularity Table.
     * @param word : Engliah word.
     * @return Popularity Score.
     */
    public int getDistinctPopularity_en(String word) {
        try {
            List<Integer> temp = jdbcTemplate.queryForList("select row_num from choldb_ver_0_1.en_popularity WHERE EnWo = '"+word+"'",  Integer.class);
            if(!temp.isEmpty())
                return temp.get(0);
        }catch (Exception e){
            logger.debug("[Chol_Repo][getDistinctPopularity_en]:"+word+"\t"+e.toString());
            e.printStackTrace();
        }
        return 0;
    }

    /***
     * To get Pleasentness Score for Tamil Word from ta_pleasentness Table.
     * @param word : tamil word.
     * @return Pleasentness Score
     */
    public int getDistinctPleasentness_ta(String word) {
        try {
            List<Integer> temp =  jdbcTemplate.queryForList("select row_num from choldb_ver_0_1.ta_pleasentness WHERE TaWo = '"+word+"'",  Integer.class);
            if(!temp.isEmpty())
                return temp.get(0);
        }catch (Exception e){
            logger.debug("[Chol_Repo][getDistinctPleasentness_ta]:"+word+"\t"+e.toString());
            e.printStackTrace();
        }
        return 0;
    }

    public Map<String, CholSearchImpl> processSearchWord(List<JSONObject> resultlist){
        Map<String, CholSearchImpl> dummydata = new HashMap<>();
        try{
            //System.out.println("Total Size:"+resultlist.size());
            int i = 0;
            for(JSONObject json : resultlist){
                /*System.out.println(json.toString());
                System.out.println(json.get("uwid_en").toString());
                System.out.println(json.get("uwid_ta").toString());
                System.out.println(json.get("pos").toString());*/
                String id = json.get("uwid_en").toString()+"-"+json.get("uwid_ta").toString()+"-"+json.get("pos").toString();
                if(!dummydata.containsKey(id)){
                    dummydata.put(id, converter.convertJSONtoBasicChol(json, new CholSearchImpl()));
                    //System.out.println("If"+json.toString());
                }else {
                    dummydata.put(id, converter.convertJSONtoBasicChol(json, dummydata.get(id)));
                    //System.out.println("Else"+json.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dummydata;
    }

    /**
     * To Get Result from Chol for a Words
     * @param word : Query Word
     * @param lang : language
     * @return list of JSON Object
     */
    public List<JSONObject> getCholResultFrmDB(String word, String lang){
        try {
            //popularity score
            if (lang.equals("ta")){
                return jdbcTemplate.query("SELECT w.word AS enwo, e.word as tawo,'hw' as wordtype, e.uwid as tamil_uwid, w.uwid as english_uwid, e.common_translit as ta_c_translit, w.common_translit as en_c_translit, e.popularity_score as tapop, w.popularity_score as enpop, dd.language_id as langid, p.pos_english AS pos, d.pos_id, dd.dtid, dd.definition, dd.example, d.udid, ds.definition_source_id from choldb_ver_0_1.definition_details as dd left join choldb_ver_0_1.definitions as d ON dd.udid = d.udid left JOIN choldb_ver_0_1.pos as p ON p.pos_id = d.pos_id left JOIN choldb_ver_0_1.definition_department as ddept ON ddept.udid_definition = d.udid left join choldb_ver_0_1.definition_source as ds ON ds.udid_definition = d.udid left JOIN choldb_ver_0_1.words as w ON w.uwid = d.english_uwid left JOIN choldb_ver_0_1.words as e on e.uwid = d.tamil_uwid WHERE e.word = '"+word.replace("'","''")+"'", new Object[]{}, new CholSearchDBIndexRowMapper());
            }else{
                return jdbcTemplate.query("SELECT w.word AS enwo, e.word as tawo,'hw' as wordtype, e.uwid as tamil_uwid, w.uwid as english_uwid, e.common_translit as ta_c_translit, w.common_translit as en_c_translit, e.popularity_score as tapop, w.popularity_score as enpop, dd.language_id as langid, p.pos_english AS pos, d.pos_id, dd.dtid, dd.definition, dd.example, d.udid, ds.definition_source_id from choldb_ver_0_1.definition_details as dd left join choldb_ver_0_1.definitions as d ON dd.udid = d.udid left JOIN choldb_ver_0_1.pos as p ON p.pos_id = d.pos_id left JOIN choldb_ver_0_1.definition_department as ddept ON ddept.udid_definition = d.udid left join choldb_ver_0_1.definition_source as ds ON ds.udid_definition = d.udid left JOIN choldb_ver_0_1.words as w ON w.uwid = d.english_uwid left JOIN choldb_ver_0_1.words as e on e.uwid = d.tamil_uwid WHERE w.word = '"+word.replace("'","''")+"'", new Object[]{}, new CholSearchDBIndexRowMapper());
            }
        }catch (Exception e){
            logger.debug("[CholSearchDao][getCholResultFrmDB]:"+word+"\t"+e.toString());
            e.printStackTrace();
        }
        return  new ArrayList<JSONObject>();
    }

    /**
     * To Get Result from Chol for a Words
     * @param word : Query Word
     * @param lang : language
     * @return list of JSON Object
     */
    public List<String> getCholResultFrmDBForDeveloper(String word, String lang){
        try {
            //popularity score
            if (lang.equals("ta")){
                return jdbcTemplate.queryForList("SELECT DISTINCT w.word AS enwo from choldb_ver_0_1.definition_details as dd left join choldb_ver_0_1.definitions as d ON dd.udid = d.udid left JOIN choldb_ver_0_1.pos as p ON p.pos_id = d.pos_id left JOIN choldb_ver_0_1.definition_department as ddept ON ddept.udid_definition = d.udid left join choldb_ver_0_1.definition_source as ds ON ds.udid_definition = d.udid left JOIN choldb_ver_0_1.words as w ON w.uwid = d.english_uwid left JOIN choldb_ver_0_1.words as e on e.uwid = d.tamil_uwid WHERE e.word = '"+word.replace("'","''")+"'", new Object[]{}, String.class);
            }else{
                return jdbcTemplate.queryForList("SELECT DISTINCT e.word as tawo from choldb_ver_0_1.definition_details as dd left join choldb_ver_0_1.definitions as d ON dd.udid = d.udid left JOIN choldb_ver_0_1.pos as p ON p.pos_id = d.pos_id left JOIN choldb_ver_0_1.definition_department as ddept ON ddept.udid_definition = d.udid left join choldb_ver_0_1.definition_source as ds ON ds.udid_definition = d.udid left JOIN choldb_ver_0_1.words as w ON w.uwid = d.english_uwid left JOIN choldb_ver_0_1.words as e on e.uwid = d.tamil_uwid WHERE w.word = '"+word.replace("'","''")+"'", new Object[]{}, String.class);
            }
        }catch (Exception e){
            logger.debug("[CholSearchDao][getCholResultFrmDB]:"+word+"\t"+e.toString());
            e.printStackTrace();
        }
        return  new ArrayList<String>();
    }

    /**
     * Get Transliterated Word.
     */
    public  String getTranslit(String word){
        try {
            //System.out.println("SELECT common_translit FROM choldb_ver_0_1.words where word = '" + word + "'");
            List<String> temp = jdbcTemplate.queryForList("SELECT common_translit FROM choldb_ver_0_1.words where word = '" + word + "' AND length(common_translit) > 0", String.class);
            return temp.size() > 0 ? temp.get(0) : "";
        } catch (Exception e){
                logger.debug("[CholSearchDao][getTranslit]"+"\t"+e.toString());
                e.printStackTrace();
        }
        return "";
    }

    /**
     * Get Transliterated Word.
     */
    public  List<JSONObject> getActualWordByTranslit(String word){
        try {
            return jdbcTemplate.query("SELECT * FROM choldb_ver_0_1.words where common_translit = '" + word + "'", new CholTranslitSearchDBIndexRowMapper());
        } catch (Exception e){
            logger.debug("[CholSearchDao][getTranslit]"+"\t"+e.toString());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}

class CholTranslitSearchDBIndexRowMapper implements RowMapper<JSONObject> {
    @Override
    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        JSONObject chol = new JSONObject();
        chol.put("uwid",rs.getString("uwid"));
        chol.put("word",rs.getString("word"));
        chol.put("lang_id",rs.getString("lang_id"));
        chol.put("is_variant",rs.getString("is_variant"));
        chol.put("common_translit",rs.getString("common_translit"));
        chol.put("popularity_score",rs.getString("popularity_score"));
        chol.put("pleasantness_score",rs.getString("pleasantness_score"));
        return chol;
    }
}

class CholSearchDBIndexRowMapper implements RowMapper<JSONObject> {
    @Override
    public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        JSONObject chol = new JSONObject();
        chol.put("udid",rs.getString("udid"));
        chol.put("enwo",rs.getString("enwo"));
        chol.put("tawo",rs.getString("tawo"));
        chol.put("uwid_ta",rs.getString("tamil_uwid"));
        chol.put("uwid_en",rs.getString("english_uwid"));
        chol.put("dtid",rs.getString("dtid"));
        chol.put("defn",rs.getString("definition"));
        chol.put("ex",rs.getString("example"));
        chol.put("ta_c_translit",rs.getString("ta_c_translit"));
        chol.put("en_c_translit",rs.getString("en_c_translit"));
        chol.put("tapop",rs.getString("tapop"));
        chol.put("enpop",rs.getString("enpop"));
        chol.put("langid",rs.getString("langid"));
        chol.put("pos",rs.getString("pos"));
        chol.put("pos_id",rs.getString("pos_id"));
        chol.put("src_id",rs.getString("definition_source_id"));
        chol.put("wordtype",rs.getString("wordtype"));
        return chol;
    }
}