package com.karefo.word.suggestion.model;

import java.util.ArrayList;
import java.util.List;

public class CholSearchImpl {

    private String tawo;
    private String enwo;
    private int ta_uwid;
    private int en_uwid;
    private String tade;
    private String ende;
    private String taex;
    private String enex;
    private String taplsnt = "0";
    private String enplsnt = "0";
    private String tapop = "0";
    private String enpop = "0";
    private String ta_c_translit = "";
    private String en_c_translit = "";
    private String pos;
    private int pos_id;
    private int pos_style  = 0;
    private int subpos = 0;
    private int udid;
    private int src_id;
    private int langid;
    private String wordtype = "";
    private String ta_Conceptual = "";
    private String en_Conceptual = "";
    private List<Department> department = new ArrayList<>();
    private String tavar = "";
    private String envar = "";
    private String tasyn = "";
    private String ensyn = "";
    private String ta_idhazhi = "";
    private String tavar_info = "";
//    private BufferedImage image;
    private String image;
    private String audio;

    private String v;
    private String vd;
    private String vdc;

    public String getTawo() {
        return tawo;
    }
    public void setTawo(String tawo) {
        this.tawo = tawo;
    }

    public String getEnwo() {
        return enwo;
    }
    public void setEnwo(String enwo) {
        this.enwo = enwo;
    }

    public int getTa_uwid() {
        return ta_uwid;
    }
    public void setTa_uwid(int ta_uwid) {
        this.ta_uwid = ta_uwid;
    }

    public int getEn_uwid() {
        return en_uwid;
    }
    public void setEn_uwid(int en_uwid) {
        this.en_uwid = en_uwid;
    }

    public String getTade() {
        return tade;
    }
    public void setTade(String tade) {
        this.tade = tade;
    }

    public String getEnde() {
        return ende;
    }
    public void setEnde(String ende) {
        this.ende = ende;
    }

    public String getTaex() {
        return taex;
    }
    public void setTaex(String taex) {
        this.taex = taex;
    }

    public String getEnex() {
        return enex;
    }
    public void setEnex(String enex) {
        this.enex = enex;
    }

    public String getPos() {
        return pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getPos_id() {
        return pos_id;
    }
    public void setPos_id(int pos_id) {
        this.pos_id = pos_id;
    }

    public int getUdid() {
        return udid;
    }
    public void setUdid(int udid) {
        this.udid = udid;
    }

    public int getSrc_id() {
        return src_id;
    }
    public void setSrc_id(int src_id) {
        this.src_id = src_id;
    }

    public String getTa_c_translit() {
        return ta_c_translit;
    }
    public void setTa_c_translit(String ta_c_translit) {
        this.ta_c_translit = ta_c_translit;
    }

    public String getEn_c_translit() {
        return en_c_translit;
    }
    public void setEn_c_translit(String en_c_translit) {
        this.en_c_translit = en_c_translit;
    }

    public int getLangid() {
        return langid;
    }
    public void setLangid(int langid) {
        this.langid = langid;
    }

    public int getSubpos() {
        return subpos;
    }
    public void setSubpos(int subpos) {
        this.subpos = subpos;
    }

    public int getPos_style() {
        return pos_style;
    }
    public void setPos_style(int pos_style) {
        this.pos_style = pos_style;
    }

    public String getTaplsnt() {
        return taplsnt;
    }
    public void setTaplsnt(String taplsnt) {
        this.taplsnt = taplsnt;
    }

    public String getEnplsnt() {
        return enplsnt;
    }
    public void setEnplsnt(String enplsnt) {
        this.enplsnt = enplsnt;
    }

    public String getTapop() {
        return tapop;
    }
    public void setTapop(String tapop) {
        this.tapop = tapop;
    }

    public String getEnpop() {
        return enpop;
    }
    public void setEnpop(String enpop) {
        this.enpop = enpop;
    }

    public String getWordtype() {return wordtype;}
    public void setWordtype(String wordtype) {this.wordtype = wordtype;}

    public String getTa_Conceptual() {
        return ta_Conceptual;
    }
    public void setTa_Conceptual(String ta_Conceptual) {
        this.ta_Conceptual = ta_Conceptual;
    }

    public String getEn_Conceptual() {
        return en_Conceptual;
    }
    public void setEn_Conceptual(String en_Conceptual) {
        this.en_Conceptual = en_Conceptual;
    }

//    public String getDepartment() {
//        return department;
//    }
//    public void setDepartment(String department) {
//        this.department = department;
//    }

    public String getTavar() {
        return tavar;
    }
    public void setTavar(String tavar) {
        this.tavar = tavar;
    }

    public String getEnvar() {
        return envar;
    }
    public void setEnvar(String envar) {
        this.envar = envar;
    }

    public String getTasyn() {
        return tasyn;
    }
    public void setTasyn(String tasyn) {
        this.tasyn = tasyn;
    }

    public String getEnsyn() {
        return ensyn;
    }
    public void setEnsyn(String ensyn) {
        this.ensyn = ensyn;
    }

    public String getTa_idhazhi() {
        return ta_idhazhi;
    }
    public void setTa_idhazhi(String ta_idhazhi) {
        this.ta_idhazhi = ta_idhazhi;
    }

    public String getTavar_info() {
        return tavar_info;
    }
    public void setTavar_info(String tavar_info) {
        this.tavar_info = tavar_info;
    }

    public String getV() {
        return v;
    }
    public void setV(String v) {
        this.v = v;
    }

    public String getVd() {
        return vd;
    }
    public void setVd(String vd) {
        this.vd = vd;
    }

    public String getVdc() {
        return vdc;
    }
    public void setVdc(String vdc) {
        this.vdc = vdc;
    }

    public List<Department> getDepartment() {
        return department;
    }
    public void setDepartment(List<Department> department) {
        this.department = department;
    }


//    public BufferedImage getImage() {
//        return image;
//    }
//    public void setImage(BufferedImage image) {
//        this.image = image;
//    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }
    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "CholSearchImpl{" +
                "tawo='" + tawo + '\'' +
                ", enwo='" + enwo + '\'' +
                ", ta_uwid=" + ta_uwid +
                ", en_uwid=" + en_uwid +
                ", tade='" + tade + '\'' +
                ", ende='" + ende + '\'' +
                ", taex='" + taex + '\'' +
                ", enex='" + enex + '\'' +
                ", taplsnt='" + taplsnt + '\'' +
                ", enplsnt='" + enplsnt + '\'' +
                ", tapop='" + tapop + '\'' +
                ", enpop='" + enpop + '\'' +
                ", ta_c_translit='" + ta_c_translit + '\'' +
                ", en_c_translit='" + en_c_translit + '\'' +
                ", pos='" + pos + '\'' +
                ", pos_id=" + pos_id +
                ", pos_style=" + pos_style +
                ", subpos=" + subpos +
                ", udid=" + udid +
                ", src_id=" + src_id +
                ", langid=" + langid +
                ", wordtype='" + wordtype + '\'' +
                ", ta_Conceptual='" + ta_Conceptual + '\'' +
                ", en_Conceptual='" + en_Conceptual + '\'' +
                ", department=" + department +
                ", tavar='" + tavar + '\'' +
                ", envar='" + envar + '\'' +
                ", tasyn='" + tasyn + '\'' +
                ", ensyn='" + ensyn + '\'' +
                ", ta_idhazhi='" + ta_idhazhi + '\'' +
                ", tavar_info='" + tavar_info + '\'' +
                ", image='" + image + '\'' +
                ", audio='" + audio + '\'' +
                ", v='" + v + '\'' +
                ", vd='" + vd + '\'' +
                ", vdc='" + vdc + '\'' +
                '}';
    }
}
