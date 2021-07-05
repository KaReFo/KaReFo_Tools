package com.karefo.audio.kiLi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@ComponentScan(basePackages={"com"})
public class KiLiUploadDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private int saveKiliData(int uwid, String voice){
        try{
            String query = "INSERT INTO choldb_ver_0_1.voice (id, uwid, voice) Values (:id, :uwid, :voice)";
            byte[] byteData = voice.replace(" ","+").getBytes("UTF-8");
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", 0)
                    .addValue("uwid",uwid )
                    .addValue("voice", byteData);
            return namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int insertKiliText(int uwid, String audioText){
        try{
            String query = "INSERT INTO choldb_ver_0_1.voice (id, uwid, voice) Values (:id, :uwid, :voice)";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", 0)
                    .addValue("uwid", uwid)
                    .addValue("voice", audioText);
            //System.out.println(">>>|"+audioText.length());
            return namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int updateKiliBase64Text(int uwid, String voiceText){
        try{
            String query = "UPDATE choldb_ver_0_1.voice SET voice =:voice WHERE uwid =:uwid";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("voice", voiceText)
                    .addValue("uwid", uwid);
            return namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<Integer> checkIsUWIDAvailable(int uwid){
        List<Integer> temp = jdbcTemplate.queryForList("SELECT uwid FROM choldb_ver_0_1.voice where uwid = '" + uwid+"'", new Object[]{}, Integer.class);
        return temp.isEmpty() ? new ArrayList<>() : temp;
    }

}
