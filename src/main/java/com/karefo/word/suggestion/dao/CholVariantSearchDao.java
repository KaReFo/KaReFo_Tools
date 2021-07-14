package com.karefo.word.suggestion.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CholVariantSearchDao {

    private static final Logger logger = LogManager.getLogger(CholVariantSearchDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int getUWID(String word, int isVariant){
        String query = "SELECT uwid FROM choldb_ver_0_1.words where word = '"+word.replace("'","''")+"' AND is_variant = "+isVariant+" ORDER BY uwid ASC;";
        List<Integer> result = jdbcTemplate.queryForList(query, Integer.class);
        return result.size() > 0 ? result.get(0) : 0;
    }


    public List<Integer> getUDIDByUWIDfrmVariantDB(int uwid) {
        try {
            return jdbcTemplate.queryForList("SELECT udid_definition FROM choldb_ver_0_1.variants where uwid_variant = ?;", new Object[]{uwid}, Integer.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public List<JSONObject> searchByUDIDfromDB(int udid){
        String query = "SELECT w.word AS enwo, e.word as tawo,'vw' as wordtype, e.uwid as tamil_uwid, w.uwid as english_uwid, e.common_translit as ta_c_translit, w.common_translit as en_c_translit, e.popularity_score as tapop, w.popularity_score as enpop, dd.language_id as langid, p.pos_english AS pos, d.pos_id, dd.dtid, dd.definition, dd.example, d.udid, ds.definition_source_id from choldb_ver_0_1.definition_details as dd left join choldb_ver_0_1.definitions as d ON dd.udid = d.udid left JOIN choldb_ver_0_1.pos as p ON p.pos_id = d.pos_id left JOIN choldb_ver_0_1.definition_department as ddept ON ddept.udid_definition = d.udid left join choldb_ver_0_1.definition_source as ds ON ds.udid_definition = d.udid left JOIN choldb_ver_0_1.words as w ON w.uwid = d.english_uwid left JOIN choldb_ver_0_1.words as e on e.uwid = d.tamil_uwid WHERE d.udid = "+udid;
        return jdbcTemplate.query(query, new CholSearchDBIndexRowMapper());
    }

    public List<JSONObject> searchVariantWord(String word){
        List<JSONObject> result = new ArrayList<>();
        try{
            //getUWID
            int uwid = getUWID(word, 1);

            //search udid from variant
            if(uwid > 0) {
                List<Integer> udids = getUDIDByUWIDfrmVariantDB(uwid);

                //get all udid's infomation
                for (int udid : udids)
                    result.addAll(searchByUDIDfromDB(udid));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}


