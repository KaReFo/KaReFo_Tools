package com.karefo.word.suggestion.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WordNotFoundDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<Integer> isWordinWordNotFound(String word) {
        try {
            return jdbcTemplate.queryForList("SELECT count from choldb_ver_0_1.word_not_found where word = '"+word+"';",  Integer.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<Integer>();
    }


    /***
     * Save Words Not Found Words In seperate DB
     * @param word
     * @return
     */
    public int insertFileNotFoundWordIntoDB(String word){
        try {
            List<Integer> result = isWordinWordNotFound(word);
            if(result.size() > 0){
                int count = result.get(0) + 1;
                return jdbcTemplate.update("UPDATE choldb_ver_0_1.word_not_found SET count = "+count+" WHERE word = '"+word+"'");
            } else {
                return jdbcTemplate.update("INSERT INTO choldb_ver_0_1.word_not_found (word, count) VALUES " + "('" + word + "'," + 1 + ")");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
