package com.karefo.audio.kural;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository("Mp3FileReaderDAO")
@EnableAutoConfiguration
@ComponentScan(basePackages={"com"})
public class Mp3FileReaderDAO {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private String convertMp3ToBase64String(File file){
        try{
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return "data:audio/mp3;base64,"+Base64.encodeBase64String(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private int updateKiliText(String kural_no, String audioText){
        try{
            String query = "UPDATE karefo.kuraldetails SET kural_audio =:kural_audio WHERE kuralNumber =:kuralNumber";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("kural_audio", audioText)
                    .addValue("kuralNumber", kural_no);
            return namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public void readMp3$SaveInDB(String path){
        try{
            File[] files = new File(path).listFiles();
            for (File file : files){
                String kural_no =  file.getName().replace(".mp3","").replaceAll("([ a-zA-Z])", "").replace("-","").trim();
                String encode = convertMp3ToBase64String(file);
                //System.out.println(String.format("%01d", Integer.parseInt(kural_no))+":"+"data:audio/ogg;base64,"+encode);
                int status = updateKiliText(String.format("%01d", Integer.parseInt(kural_no)), encode);
                if(status != 1){
                    //System.out.println(kural_no);
                }
            }
            System.out.println("Completed...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        ConfigurableApplicationContext configurableApplicationContext= SpringApplication.run(Mp3FileReaderDAO.class, args);
        Mp3FileReaderDAO startCholIndex = configurableApplicationContext.getBean(Mp3FileReaderDAO.class);
        startCholIndex.readMp3$SaveInDB("C:\\Users\\Admin\\Desktop\\MP3\\");
    }

}
