package com.karefo.image.dao;

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
public class ImageUploadDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private int saveImageData(int udid, String image){
        try{
            String query = "INSERT INTO choldb_ver_0_1.image (id, udid, image) Values (:id, :udid, :image)";
            byte[] byteData = image.replace(" ","+").getBytes("UTF-8");
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", 0)
                    .addValue("udid",udid )
                    .addValue("image", byteData);
            return namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    /*public int SaveIamge(String inputString){
        try{
            String[] inputSplit = inputString.split("&");
            int udid = 0;
            String imageString = "";
            for(String input : inputSplit){
                if(input.contains("udid="))
                    udid = Integer.parseInt(input.substring(input.indexOf("=")+1, input.length()).replace(".png","").replace(".jpg","").replace(".jpeg","").replace(".tif",""));
                else if(input.contains("image="))
                    imageString = input.substring(input.indexOf("=")+1, input.length());
            }
            return saveImageData(udid, imageString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }*/

    public int insertImageText(int udid, String imageText){
        try{
            String query = "INSERT INTO choldb_ver_0_1.image (id, udid, image) Values (:id, :udid, :image)";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", 0)
                    .addValue("udid", udid)
                    .addValue("image", imageText);
            return namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public int updateImageBase64Text(int udid, String audioText){
        try{
            String query = "UPDATE choldb_ver_0_1.image SET image =:image WHERE udid =:udid";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("image", audioText)
                    .addValue("udid", udid);
            return namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<Integer> checkIsUWIDAvailable(int udid){
        List<Integer> temp = jdbcTemplate.queryForList("SELECT udid FROM choldb_ver_0_1.image where udid = '" + udid+"'", new Object[]{}, Integer.class);
        return temp.isEmpty() ? new ArrayList<>() : temp;
    }


}
