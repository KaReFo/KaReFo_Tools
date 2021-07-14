package com.karefo.word.suggestion.dao;

import com.karefo.word.suggestion.model.CholSearchImpl;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CholSortingAlgorithm {



    /*Sort By Popularity*/
    private void sortByPopularity(List<CholSearchImpl> dblistyettosort, List<CholSearchImpl> result, String lang){
        if(!dblistyettosort.isEmpty()) {
            if(lang.equals("en"))
                dblistyettosort.sort(new CholComparatorByTamil());
            else
                dblistyettosort.sort(new CholComparatorByEnglish());
            result.addAll(dblistyettosort);

        }
    }

    /**
     *Chol Sorting Algorithm
     */
    public List<CholSearchImpl> sortData(String queryWord, List<CholSearchImpl> fromdb, String lang){
        List<CholSearchImpl> result = new ArrayList<>();

        List<CholSearchImpl> chol_hw_defn_both = new ArrayList<>();
        List<CholSearchImpl> chol_hw_defn_anyone = new ArrayList<>();
        List<CholSearchImpl> chol_hw = new ArrayList<>();
        List<CholSearchImpl> chol_vw_defn_both = new ArrayList<>();
        List<CholSearchImpl> chol_vw_defn_anyone = new ArrayList<>();
        List<CholSearchImpl> chol_vw = new ArrayList<>();

        List<CholSearchImpl> tvu_hw_defn_both = new ArrayList<>();
        List<CholSearchImpl> tvu_hw_defn_anyone = new ArrayList<>();
        List<CholSearchImpl> tvu_hw = new ArrayList<>();
        List<CholSearchImpl> tvu_vw_defn_both = new ArrayList<>();
        List<CholSearchImpl> tvu_vw_defn_anyone = new ArrayList<>();
        List<CholSearchImpl> tvu_vw = new ArrayList<>();

        try{

            for(CholSearchImpl json : fromdb){
                if(json.getSrc_id() == 1){
                    switch (json.getWordtype()){
                        case "hw":
                            if(json.getTade().length() > 1 && json.getEnde().length() > 1)
                                chol_hw_defn_both.add(json);
                            else if(json.getTade().length() > 1 || json.getEnde().length() > 1)
                                chol_hw_defn_anyone.add(json);
                            else
                                chol_hw.add(json);
                            break;
                        case "vw":
                            if(json.getTade().length() > 1 && json.getEnde().length() > 1)
                                chol_vw_defn_both.add(json);
                            else if(json.getTade().length() > 1 || json.getEnde().length() > 1)
                                chol_vw_defn_anyone.add(json);
                            else
                                chol_vw.add(json);
                            break;
                    }
                } else if (json.getSrc_id() == 2){
                    switch (json.getWordtype()){
                        case "hw":
                            if((json.getTade().length() > 1 && json.getEnde().length() > 1 )  && (json.getTade().replaceAll("\\(.*?\\)", "").equals(json.getTade()) && json.getEnde().replaceAll("\\(.*?\\)", "").equals(json.getEnde())))
                                chol_hw_defn_both.add(json);
                            else if(json.getTade().length() > 1 && json.getEnde().length() > 1)
                                tvu_hw_defn_both.add(json);
                            else if((json.getTade().length() > 1 || json.getEnde().length() > 1) && (json.getTade().replaceAll("\\(.*?\\)", "").equals(json.getTade()) || json.getEnde().replaceAll("\\(.*?\\)", "").equals(json.getEnde())))
                                chol_hw_defn_anyone.add(json);
                            else if(json.getTade().length() > 1 || json.getEnde().length() > 1)
                                tvu_hw_defn_anyone.add(json);
                            else
                                tvu_hw.add(json);
                            break;
                        case "vw":
                            if((json.getTade().length() > 1 && json.getEnde().length() > 1)  && (json.getTade().replaceAll("\\(.*?\\)", "").equals(json.getTade()) && json.getEnde().replaceAll("\\(.*?\\)", "").equals(json.getEnde())))
                                chol_vw_defn_both.add(json);
                            else if((json.getTade().length() > 1 && json.getEnde().length() > 1))
                                tvu_vw_defn_both.add(json);
                            else if((json.getTade().length() > 1 || json.getEnde().length() > 1)  && (json.getTade().replaceAll("\\(.*?\\)", "").equals(json.getTade()) || json.getEnde().replaceAll("\\(.*?\\)", "").equals(json.getEnde())) )
                                chol_vw_defn_anyone.add(json);
                            else if((json.getTade().length() > 1 || json.getEnde().length() > 1))
                                tvu_vw_defn_anyone.add(json);
                            else
                                tvu_vw.add(json);
                            break;
                    }
                }else {
                    switch (json.getWordtype()){
                        case "hw":
                            if(json.getTade().length() > 1 && json.getEnde().length() > 1)
                                tvu_hw_defn_both.add(json);
                            else if(json.getTade().length() > 1 || json.getEnde().length() > 1)
                                tvu_hw_defn_anyone.add(json);
                            else
                                tvu_hw.add(json);
                            break;
                        case "vw":
                            if(json.getTade().length() > 1 && json.getEnde().length() > 1)
                                tvu_vw_defn_both.add(json);
                            else if(json.getTade().length() > 1 || json.getEnde().length() > 1)
                                tvu_vw_defn_anyone.add(json);
                            else
                                tvu_vw.add(json);
                            break;
                    }
                }
            }

            //Rank 1 : chol entry; has both definition; ranking based on popularity; Headword search
            sortByPopularity(chol_hw_defn_both, result, lang);

            //Rank 2 : chol entry; has atleast one definition; ranking based on popularity; Headword search
            sortByPopularity(chol_hw_defn_anyone, result, lang);

            //Rank 3 : chol entry; has no definition; ranking based on popularity; Headword search
            sortByPopularity(chol_hw, result, lang);

            //Rank 4 : chol entry; has both definition; ranking based on popularity; Variantword search
            sortByPopularity(chol_vw_defn_both, result, lang);

            //Rank 5 : chol entry; has atleast one definition; ranking based on popularity; Variantword search
            sortByPopularity(chol_vw_defn_anyone, result, lang);

            //Rank 6 : chol entry; has no definition; ranking based on popularity; Variantword search
            sortByPopularity(chol_vw, result, lang);

            //Rank 7 : TVU entry; has both definition; ranking based on popularity; Headword search
            sortByPopularity(tvu_hw_defn_both, result, lang);

            //Rank 8 : TVU entry; has atleast one definition; ranking based on popularity; Headword search
            sortByPopularity(tvu_hw_defn_anyone, result, lang);

            //Rank 9 : TVU entry; has no definition; ranking based on popularity; Headword search
            sortByPopularity(tvu_hw, result, lang);

            //Rank 10 : TVU entry; has both definition; ranking based on popularity; Variantword search
            sortByPopularity(tvu_vw_defn_both, result, lang);

            //Rank 11 : TVU entry; has atleast one definition; ranking based on popularity; Variantword search
            sortByPopularity(tvu_vw_defn_anyone, result, lang);

            //Rank 12 : TVU entry; has no definition; ranking based on popularity; Variantword search
            sortByPopularity(tvu_vw, result, lang);

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

//    /**
//     *
//     */
//    private List<JSONObject> sortTamilData(String queryWord, List<JSONObject> fromdb){
//        List<JSONObject> result = new ArrayList<JSONObject>();
//        List<JSONObject> HwWithBothDefn = new ArrayList<JSONObject>();
//        List<JSONObject> HwWithDefn = new ArrayList<JSONObject>();
//        List<JSONObject> HwOnly = new ArrayList<JSONObject>();
//        List<JSONObject> VarWithBothDefn = new ArrayList<JSONObject>();
//        List<JSONObject> VarWithDefn = new ArrayList<JSONObject>();
//        List<JSONObject> VarOnly = new ArrayList<JSONObject>();
//        try{
//            for(JSONObject json : fromdb){
//                String TaWo = json.get("TaWo").toString().trim();
//                String TaDe = json.get("TaDe").toString().trim();
//                String EnDe = json.get("EnDe").toString().trim();
//
//                //Query Word === Headword
//                if(queryWord.equals(TaWo)){
//                    if(TaDe.length() > 1 && EnDe.length() > 1){
//                        HwWithBothDefn.add(json);
//                    }else if(TaDe.length() > 1 || EnDe.length() > 1){
//                        HwWithDefn.add(json);
//                    }else{
//                        HwOnly.add(json);
//                    }
//                } else { //Query Word === Variant
//                    if(TaDe.length() > 1 && EnDe.length() > 1){
//                        VarWithBothDefn.add(json);
//                    }else if(TaDe.length() > 1 || EnDe.length() > 1){
//                        VarWithDefn.add(json);
//                    }else{
//                        VarOnly.add(json);
//                    }
//                }
//            }
//            HwWithBothDefn.sort(new CholComparatorByTamil());
//            VarWithBothDefn.sort(new CholComparatorByTamil());
//            HwWithDefn.sort(new CholComparatorByTamil());
//            VarWithDefn.sort(new CholComparatorByTamil());
//            HwOnly.sort(new CholComparatorByTamil());
//            VarOnly.sort(new CholComparatorByTamil());
//
//            result.addAll(HwWithBothDefn);
//            result.addAll(VarWithBothDefn);
//            result.addAll(HwWithDefn);
//            result.addAll(VarWithDefn);
//            result.addAll(HwOnly);
//            result.addAll(VarOnly);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//    private List<JSONObject> sortEnglishData(String queryWord, List<JSONObject> fromdb){
//        List<JSONObject> result = new ArrayList<JSONObject>();
//        List<JSONObject> HwWithBothDefn = new ArrayList<JSONObject>();
//        List<JSONObject> HwWithDefn = new ArrayList<JSONObject>();
//        List<JSONObject> HwOnly = new ArrayList<JSONObject>();
//        List<JSONObject> VarWithBothDefn = new ArrayList<JSONObject>();
//        List<JSONObject> VarWithDefn = new ArrayList<JSONObject>();
//        List<JSONObject> VarOnly = new ArrayList<JSONObject>();
//        try{
//            for(JSONObject json : fromdb){
//                String EnWo = json.get("EnWo").toString().trim();
//                String TaDe = json.get("TaDe").toString().trim();
//                String EnDe = json.get("EnDe").toString().trim();
//
//                //Query Word === Headword
//                if(queryWord.equals(EnWo)){
//                    if(TaDe.length() > 1 && EnDe.length() > 1){
//                        HwWithBothDefn.add(json);
//                    }else if(TaDe.length() > 1 || EnDe.length() > 1){
//                        HwWithDefn.add(json);
//                    }else{
//                        HwOnly.add(json);
//                    }
//                } else { //Query Word === Variant
//                    if(TaDe.length() > 1 && EnDe.length() > 1){
//                        VarWithBothDefn.add(json);
//                    }else if(TaDe.length() > 1 || EnDe.length() > 1){
//                        VarWithDefn.add(json);
//                    }else{
//                        VarOnly.add(json);
//                    }
//                }
//            }
//            HwWithBothDefn.sort(new CholComparatorByTamil());
//            VarWithBothDefn.sort(new CholComparatorByTamil());
//            HwWithDefn.sort(new CholComparatorByTamil());
//            VarWithDefn.sort(new CholComparatorByTamil());
//            HwOnly.sort(new CholComparatorByTamil());
//            VarOnly.sort(new CholComparatorByTamil());
//
//            result.addAll(HwWithBothDefn);
//            result.addAll(VarWithBothDefn);
//            result.addAll(HwWithDefn);
//            result.addAll(VarWithDefn);
//            result.addAll(HwOnly);
//            result.addAll(VarOnly);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return result;
//    }

}
