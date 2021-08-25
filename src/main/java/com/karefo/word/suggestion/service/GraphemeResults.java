package com.karefo.word.suggestion.service;

import com.karefo.word.suggestion.model.CholSearchImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class GraphemeResults {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    Workbook workbook=null;
    ArrayList<HashMap<String, ArrayList<String>>> result=new ArrayList<HashMap<String, ArrayList<String>>>();




    public  void loader() {      //loads the file from resources

        try {
            InputStream res = getClass().getResourceAsStream("/Grapheme_Pattern.xlsx");

            workbook= new XSSFWorkbook(res);
            readsheet();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void readsheet(){


        //Built in classes formatting cell values
        DataFormatter objDefaultFormat = new DataFormatter();
        FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);


        //Get first/desired sheet from the workbook
        Sheet sheet = workbook.getSheet("Sheet2");



        for(int i=1;i<sheet.getLastRowNum();i++) {
            //Gets Row
            Row row = sheet.getRow(i);
            ArrayList<String> list = new ArrayList<String>();

            for (int j = 1; j < row.getLastCellNum(); j++) {
                //Gets the value of the first cell and formatted to string
                Cell ip = row.getCell(j);
                objFormulaEvaluator.evaluate(ip);
                String ipaval = objDefaultFormat.formatCellValue(ip, objFormulaEvaluator);
                ipaval = ipaval.trim();

                if(!ipaval.isEmpty()){
                    list.add(ipaval);
                }

            }


            Cell ip = row.getCell(0);
            objFormulaEvaluator.evaluate(ip);
            String ipaval = objDefaultFormat.formatCellValue(ip, objFormulaEvaluator);
            ipaval = ipaval.trim();
            if(ipaval.isEmpty()){
                break;
            }
            HashMap<String, ArrayList<String>> curr = new HashMap<String, ArrayList<String>>();

            curr.put(ipaval, list);
            result.add(curr);




        }

    }




    public ArrayList<HashMap<String,ArrayList<String>>> getScores(){
        return result;
    }


//input
    public ArrayList<String> graphemepattern(String input){
        loader();
        ArrayList<String> letters= tamilsplitWord(input);

        ArrayList<String> possiblewords=  graphemeEdit(letters,input);

        ArrayList<String> possiblePatterns=  getPatterns(possiblewords);



        return  getRelatedPatterns(possiblePatterns);
    }
    public  ArrayList<String> getRelatedPatterns( ArrayList<String> patterns){
        ArrayList<String> wordslist=new ArrayList<String>();
        for (int i=0;i<patterns.size();i++){
            //get word and pattern matching this pattern from db
            List<CholSearchImpl> words=  getGraphemeScoreWords(patterns.get(i));

            for(int j=0;j<words.size();j++){
                wordslist.add(words.get(j).getTawo());
            }


        }
        LinkedHashSet<String> hashSet = new LinkedHashSet<String>(wordslist);

        wordslist = new ArrayList<String>(hashSet);
        return wordslist;
    }
    public List<CholSearchImpl> getGraphemeScoreWords(String score){///NEED to CHANGE TABLE
        String query="SELECT word,uwid,grapheme_score FROM grapheme_score_db.words where grapheme_score=:score order by popularity_score desc ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("score", score);


        return namedParameterJdbcTemplate.query(query, parameters,new CholGraphemeDBIndexRowMapper());


    }

    public ArrayList<String> getPatterns(ArrayList<String> words){
        ArrayList<String> patterns=new ArrayList<String>();

        for (int i=0;i<words.size();i++){
            patterns.add(getgraphemePattern(words.get(i)));
        }

        return patterns;
    }
    public String getgraphemePattern(String word){
        ArrayList<String> letters=tamilsplitWord(word);
        return findscore(letters);
    }
    public String findscore(ArrayList<String> letters){
        String result="";
        for (int i=0;i<letters.size();i++){
            result+=getScoreofletter(letters.get(i));
        }


        return result;
    }

    public String getScoreofletter(String letter){
        String res="";
        for (int i=0;i<result.size();i++) {
            int k = 0;

            for (Map.Entry<String, ArrayList<String>> entry : result.get(i).entrySet()) {
                for (int j = 0; j < entry.getValue().size(); j++) {
                    if (entry.getValue().get(j).equals(letter)) {
                        k = 1;
                        break;
                    }
                }

                if (k == 1) {
                    res =entry.getKey();
                    break;
                }
            }
        }

        return res;
    }



    public String replacegrapheme(ArrayList<String> letters,String replace,int i ){
        letters.remove(i);
        letters.remove(i);
        letters.add(i,replace);
        String res="";
        for(int j=0;j<letters.size();j++){
            res+=letters.get(j);
        }

        return res;

    } public   ArrayList<String> graphemeEdit(ArrayList<String> letters,String res){

        ArrayList<String> results=new ArrayList<String>();
        int k=0;

        for(int i=0;i<letters.size()-1;i++){

            String t1=letters.get(i);
            String t2=letters.get(i+1);

            if(t1.equals("ச") && t2.equals("உ") ){
                results.add(replacegrapheme(letters,"கூ",i));


            }
            else if(t1.equals("உ") && t2.equals("ள") ){

                results.add(replacegrapheme(letters,"ஊ",i));
            }
            else if(t1.equals("உ") && t2.equals("ற") ){
                results.add(replacegrapheme(letters,"ஹ",i));


            }

            else if(t1.equals("உ") && t2.equals("ர்") ){
                results.add(replacegrapheme(letters,"ஷ்",i));

            }

            else if(t1.equals("ச") && t2.equals("ு") ){
                results.add(replacegrapheme(letters,"க",i));

            }



        }

        results.add(res);

        return results;
    }
    public   ArrayList<String> graphemeEdit1(ArrayList<String> letters){
        String res="";
        ArrayList<String> results=new ArrayList<String>();
        int k=0;
        for(int i=0;i<letters.size();i++){
            res+=letters.get(i);
        }
        for(int i=0;i<letters.size()-1;i++){

            String t1=letters.get(i);
            String t2=letters.get(i+1);

            if(t1.equals("ச") && t2.equals("உ") ){
                results.add(replacegrapheme(letters,"கூ",i));


            }
            else if(t1.equals("உ") && t2.equals("ள") ){

                results.add(replacegrapheme(letters,"ஊ",i));
            }
            else if(t1.equals("உ") && t2.equals("ற") ){
                results.add(replacegrapheme(letters,"ஹ",i));


            }

            else if(t1.equals("உ") && t2.equals("ர்") ){
                results.add(replacegrapheme(letters,"ஷ்",i));

            }

            else if(t1.equals("ச") && t2.equals("ு") ){
                results.add(replacegrapheme(letters,"க",i));

            }



        }

        results.add(res);

        return results;
    }

    public ArrayList<String> tamilsplitWord(String Wordsarray) // method to split the words to list of tamil letters

    {
        ArrayList<String> resultArray = new ArrayList<String>();
        try {
            String fin = "";
            String temp = "";
            int size_word = 0;
            String tString = "", tStringNext = "", tempString, prevString = "";

            for (int j = 0; j < Wordsarray.length(); j++) {
                // //System.out.println("prevString"+prevString);
                tString = Wordsarray.substring(j, j + 1);

                for (int k = j + 1; k < Wordsarray.length(); k++) {
                    if (j < Wordsarray.length() - 1)
                        tStringNext = Wordsarray.substring(j + 1, j + 2);
                    else
                        tStringNext = "";
                    if (tStringNext.equals("\u0BBE")
                            || tStringNext.equals("\u0BBF")
                            || tStringNext.equals("\u0BC0")
                            || tStringNext.equals("\u0BC1")
                            || tStringNext.equals("\u0BC2")
                            || tStringNext.equals("\u0BC6")
                            || tStringNext.equals("\u0BC7")
                            || tStringNext.equals("\u0BC8")
                            || tStringNext.equals("\u0BCA")
                            || tStringNext.equals("\u0BCB")
                            || tStringNext.equals("\u0BCC")
                            || tStringNext.equals("\u0BCD")
                    ) {
                        tString = tString + tStringNext;
                        j++;
                    } else {
                        k = Wordsarray.length() - 1;
                    }

                }

                resultArray.add(tString);
            }
        }

        catch (Exception e) {
        }
        resultArray.remove(" ");
        return splitTamilChar(resultArray);
    }
    public ArrayList<String> splitTamilChar(  ArrayList<String> input){
        ArrayList<String> letters=new ArrayList<String>();

        for(int i=0;i<input.size();i++){

            if(input.get(i).length()==2 && !(input.get(i).charAt(1)+"").equals("்") ){

                String a=input.get(i).charAt(0)+"";
                String b=input.get(i).charAt(1)+"";

                if(b.equals("ொ") ){
                    letters.add("ெ");
                    letters.add(a);
                    letters.add("ா");
                }
                else if( b.equals("ோ") ){
                    letters.add("ே");
                    letters.add(a);
                    letters.add("ா");
                }
                else if( b.equals("ௌ")){
                    letters.add("ெ");
                    letters.add(a);
                    letters.add("ள");
                }
                else if( b.equals("ெ")){
                    letters.add("ெ");
                    letters.add(a);

                }
                else if( b.equals("ே")){
                    letters.add("ே");
                    letters.add(a);

                }
                else if( b.equals("ை")){
                    letters.add("ை");
                    letters.add(a);

                }
                else{

                    letters.add(a);
                    letters.add(b);
                }

            }else{
                letters.add(input.get(i));

            }

        }

        return letters;
    }












}

class CholGraphemeDBIndexRowMapper implements RowMapper<CholSearchImpl> {
    @Override
    public CholSearchImpl mapRow(ResultSet rs, int rowNum) throws SQLException {
        CholSearchImpl word = new CholSearchImpl();

        word.setTa_uwid(Integer.parseInt(rs.getString("uwid")));
        word.setTawo(rs.getString("word"));
        word.setGrapheme_score(rs.getString("grapheme_score"));




        return word;
    }
}