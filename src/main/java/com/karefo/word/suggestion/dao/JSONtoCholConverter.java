package com.karefo.word.suggestion.dao;

import com.karefo.word.suggestion.model.CholSearchImpl;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public class JSONtoCholConverter {

/*    public CholSearchImpl convertJSONtoChol(JSONObject json, CholSearchImpl chol){
        if(json.get("langid").toString().equals("1") ) {
            chol.setUdid(json.get("udid")!=null?Integer.parseInt(json.get("udid").toString()):0);
            chol.setTawo(json.get("tawo")!=null?json.get("tawo").toString():"");
            chol.setEnwo(json.get("enwo")!=null?json.get("enwo").toString():"");
            chol.setTa_uwid(json.get("uwid_ta")!=null?Integer.parseInt(json.get("uwid_ta").toString()):0);
            chol.setEn_uwid(json.get("uwid_en")!=null?Integer.parseInt(json.get("uwid_en").toString()):0);
            chol.setTade(json.get("defn")!=null?json.get("defn").toString():"");
            chol.setTaex(json.get("ex")!=null?json.get("ex").toString():"");
            chol.setTa_etymo(json.get("taety")!=null?json.get("taety").toString():"");
            chol.setEn_etymo(json.get("enety")!=null?json.get("enety").toString():"");
            chol.setTa_c_translit(json.get("ta_c_translit")!=null?json.get("ta_c_translit").toString():"");
            chol.setEn_c_translit(json.get("en_c_translit")!=null?json.get("en_c_translit").toString():"");
            chol.setTa_occur(json.get("taocc")!=null?json.get("taocc").toString():"");
            chol.setEn_occur(json.get("enocc")!=null?json.get("enocc").toString():"");
            chol.setLangid(json.get("langid")!=null?Integer.parseInt(json.get("langid").toString()):0);
            chol.setPos(json.get("pos")!=null?json.get("pos").toString():"");
            chol.setPos_id(json.get("pos_id")!=null?Integer.parseInt(json.get("pos_id").toString()):0);
            chol.setDept(json.get("dept")!=null?json.get("dept").toString():"");
            chol.setTavar(json.get("tavar")!=null?json.get("tavar").toString():"");
            chol.setEnvar(json.get("envar")!=null?json.get("envar").toString():"");
            chol.setTasyn(json.get("tasyn")!=null?json.get("tasyn").toString():"");
            chol.setUnl(json.get("unl")!=null?json.get("unl").toString():"");
            chol.setVerifyStatus(json.get("verifyStatus")!=null?Integer.parseInt(json.get("verifyStatus").toString()):0);
            chol.setNote(json.get("notes")!=null?json.get("notes").toString():"");
            chol.setSubpos(json.get("subpos")!=null?Integer.parseInt(json.get("subpos").toString()):0);
            chol.setPos_style(json.get("pos_style")!=null?Integer.parseInt(json.get("pos_style").toString()):0);
            chol.setSource_group_id(json.get("source_group_id")!=null?Integer.parseInt(json.get("source_group_id").toString()):0);
            chol.setSrc_id(json.get("src_id")!=null?Integer.parseInt(json.get("src_id").toString()):0);
        } else {
            chol.setUdid(json.get("udid")!=null?Integer.parseInt(json.get("udid").toString()):0);
            chol.setTawo(json.get("tawo")!=null?json.get("tawo").toString():"");
            chol.setEnwo(json.get("enwo")!=null?json.get("enwo").toString():"");
            chol.setTa_uwid(json.get("uwid_ta")!=null?Integer.parseInt(json.get("uwid_ta").toString()):0);
            chol.setEn_uwid(json.get("uwid_en")!=null?Integer.parseInt(json.get("uwid_en").toString()):0);
            chol.setEnde(json.get("defn")!=null?json.get("defn").toString():"");
            chol.setEnex(json.get("ex")!=null?json.get("ex").toString():"");
            chol.setTa_etymo(json.get("taety")!=null?json.get("taety").toString():"");
            chol.setEn_etymo(json.get("enety")!=null?json.get("enety").toString():"");
            chol.setTa_c_translit(json.get("ta_c_translit")!=null?json.get("ta_c_translit").toString():"");
            chol.setEn_c_translit(json.get("en_c_translit")!=null?json.get("en_c_translit").toString():"");
            chol.setTa_occur(json.get("taocc")!=null?json.get("taocc").toString():"");
            chol.setEn_occur(json.get("enocc")!=null?json.get("enocc").toString():"");
            chol.setLangid(json.get("langid")!=null?Integer.parseInt(json.get("langid").toString()):0);
            chol.setPos(json.get("pos")!=null?json.get("pos").toString():"");
            chol.setPos_id(json.get("pos_id")!=null?Integer.parseInt(json.get("pos_id").toString()):0);
            chol.setDept(json.get("dept")!=null?json.get("dept").toString():"");
            chol.setTavar(json.get("tavar")!=null?json.get("tavar").toString():"");
            chol.setEnvar(json.get("envar")!=null?json.get("envar").toString():"");
            chol.setEnsyn(json.get("ensyn")!=null?json.get("ensyn").toString():"");
            chol.setUnl(json.get("unl")!=null?json.get("unl").toString():"");
            chol.setVerifyStatus(json.get("verifyStatus")!=null?Integer.parseInt(json.get("verifyStatus").toString()):0);
            chol.setNote(json.get("notes")!=null?json.get("notes").toString():"");
            chol.setSubpos(json.get("subpos")!=null?Integer.parseInt(json.get("subpos").toString()):0);
            chol.setPos_style(json.get("pos_style")!=null?Integer.parseInt(json.get("pos_style").toString()):0);
            chol.setSource_group_id(json.get("source_group_id")!=null?Integer.parseInt(json.get("source_group_id").toString()):0);
            chol.setSrc_id(json.get("src_id")!=null?Integer.parseInt(json.get("src_id").toString()):0);
        }
        return chol;
    }*/


    public CholSearchImpl convertJSONtoBasicChol(JSONObject json, CholSearchImpl chol){
        if(json.get("langid").toString().equals("1") ) {
            chol.setUdid(json.get("udid")!=null?Integer.parseInt(json.get("udid").toString()):0);
            chol.setTawo(json.get("tawo")!=null?json.get("tawo").toString():"");
            chol.setEnwo(json.get("enwo")!=null?json.get("enwo").toString():"");
            chol.setTade(json.get("defn")!=null?json.get("defn").toString():"");
            chol.setTaex(json.get("ex")!=null?json.get("ex").toString():"");
            chol.setTa_c_translit(json.get("ta_c_translit")!=null?json.get("ta_c_translit").toString():"");
            chol.setEn_c_translit(json.get("en_c_translit")!=null?json.get("en_c_translit").toString():"");
            chol.setLangid(json.get("langid")!=null?Integer.parseInt(json.get("langid").toString()):0);
            chol.setPos(json.get("pos")!=null?json.get("pos").toString():"");
            chol.setPos_id(json.get("pos_id")!=null?Integer.parseInt(json.get("pos_id").toString()):0);
            chol.setSrc_id(json.get("src_id")!=null?Integer.parseInt(json.get("src_id").toString()):0);

            chol.setTapop(json.get("tapop")!=null?json.get("tapop").toString():"0");
            chol.setEnpop(json.get("enpop")!=null?json.get("enpop").toString():"0");
            chol.setWordtype(json.get("wordtype")!=null?json.get("wordtype").toString():"");
        } else {
            chol.setUdid(json.get("udid")!=null?Integer.parseInt(json.get("udid").toString()):0);
            chol.setTawo(json.get("tawo")!=null?json.get("tawo").toString():"");
            chol.setEnwo(json.get("enwo")!=null?json.get("enwo").toString():"");
            chol.setEnde(json.get("defn")!=null?json.get("defn").toString():"");
            chol.setEnex(json.get("ex")!=null?json.get("ex").toString():"");
            chol.setTa_c_translit(json.get("ta_c_translit")!=null?json.get("ta_c_translit").toString():"");
            chol.setEn_c_translit(json.get("en_c_translit")!=null?json.get("en_c_translit").toString():"");
            chol.setLangid(json.get("langid")!=null?Integer.parseInt(json.get("langid").toString()):0);
            chol.setPos(json.get("pos")!=null?json.get("pos").toString():"");
            chol.setPos_id(json.get("pos_id")!=null?Integer.parseInt(json.get("pos_id").toString()):0);
            chol.setSrc_id(json.get("src_id")!=null?Integer.parseInt(json.get("src_id").toString()):0);

            chol.setTapop(json.get("tapop")!=null?json.get("tapop").toString():"0");
            chol.setEnpop(json.get("enpop")!=null?json.get("enpop").toString():"0");
            chol.setWordtype(json.get("wordtype")!=null?json.get("wordtype").toString():"");
        }
        return chol;
    }
}
