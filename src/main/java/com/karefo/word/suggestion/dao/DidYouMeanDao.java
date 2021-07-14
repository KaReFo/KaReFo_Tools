package com.karefo.word.suggestion.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DidYouMeanDao {

    private static final Logger logger = LogManager.getLogger(DidYouMeanDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<String> getDidYouMean(String word, String lang){
        if(lang.equals("ta"))
            return getDidYouMean_Ta(word);
        else
            return getDidYouMean_En(word);
    }

    /**
     * To check whether given tamil query is miss spelt Word or miss typed word
     * @param word : query word
     * @return word Suggesion
     */
    private List<String> getDidYouMean_Ta(String word){
        return jdbcTemplate.queryForList("SELECT DISTINCT TaWo FROM KaReFo.Chol_Spell_Suggestion_Ta where MisspeltWord LIKE '%|"+word+"|%'",new Object[]{}, String.class);
    }


    /**
     * To check whether given English query is miss spelt Word or miss typed word
     * @param word : query word
     * @return word Suggesion
     */
    private List<String> getDidYouMean_En(String word){
        return jdbcTemplate.queryForList("SELECT DISTINCT EnWo FROM KaReFo.Chol_Spell_Suggestion_En where MisspeltWord LIKE '%|"+word+"|%'",new Object[]{}, String.class);
    }
}
