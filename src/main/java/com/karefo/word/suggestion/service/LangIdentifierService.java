package com.karefo.word.suggestion.service;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service("LangIdentifierService")
@ComponentScan(basePackages = "com")
public class LangIdentifierService {

    /**
     * Check given input is Numeric
     * @param input
     * @return
     */
    private boolean isNumeric(String input) {
        return input.matches("-?\\d+(\\.\\d+)?");
    }


    /**
     * Check given input is Tamil text
     * Note : Have to change best way better code***
     * @param input
     * @return
     */
    private boolean isTamil(String input) {
//        String[] inputList = input.split("");
//        int i = 0;
//        for(String letter : inputList) {
//            if(letter.matches("[ அ்ஆாஇிஈீஉுஊூஎெஏேஐைஒொஓோஔௌஃகஙசஞடணதநபமயரலவழளறனஜஷஸஹ]")){
//                i++;
//            }
//        }
//        return i == input.length();
        Pattern pattern = Pattern.compile("[ஃ-் ]+");
        return pattern.matcher(input).matches();
    }




    /**
     * Check given input is English text
     * @param input
     * @return
     */
    private boolean isEnglish(String input) {
        Pattern pattern = Pattern.compile("[a-zA-Z \\-]+");
        return pattern.matcher(input).matches();
    }


    /**
     * Check given input Belong to English (or) Tamil (or) Numeric
     * @param input
     * @return
     */
    public String identifyLanguage(String input){
        try{
            if(isNumeric(input))
                return "num";
            else if(isEnglish(input))
                return "en";
            else if(isTamil(input))
                return "ta";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "invalid";
    }

/*    public static void main(String args[]){
        ConfigurableApplicationContext configurableApplicationContext= SpringApplication.run(LangIdentifierService.class, args);
        LangIdentifierService aadugalamService=configurableApplicationContext.getBean(LangIdentifierService.class);
        String temp = aadugalamService.identifyLanguage("-1.5");
        System.out.println(temp);
    }*/
}
