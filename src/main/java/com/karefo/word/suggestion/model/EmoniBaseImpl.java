package com.karefo.word.suggestion.model;

public class EmoniBaseImpl {

    private String eng_word = "";
    private String tam_word = "";
    private String pos = "";

    public String getEng_word() {
        return eng_word;
    }
    public void setEng_word(String eng_word) {
        this.eng_word = eng_word;
    }

    public String getTam_word() {
        return tam_word;
    }
    public void setTam_word(String tam_word) {
        this.tam_word = tam_word;
    }

    public String getPos() {
        return pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "EmoniBaseImpl{" +
                "eng_word='" + eng_word + '\'' +
                ", tam_word='" + tam_word + '\'' +
                ", pos='" + pos + '\'' +
                '}';
    }
}
