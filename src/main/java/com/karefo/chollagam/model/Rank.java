package com.karefo.chollagam.model;

import java.io.Serializable;
import java.util.List;

public class Rank implements Serializable {

    private String word;
    private int uwid;
    private List<Integer> udid_list;
    private boolean headword;
    private byte[] result;

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

    public List<Integer> getUdid_list() {
        return udid_list;
    }
    public void setUdid_list(List<Integer> udid_list) {
        this.udid_list = udid_list;
    }

    public int getUwid() {
        return uwid;
    }
    public void setUwid(int uwid) {
        this.uwid = uwid;
    }

    public boolean isHeadword() {
        return headword;
    }
    public void setHeadword(boolean headword) {
        this.headword = headword;
    }

    public byte[] getResult() {
        return result;
    }
    public void setResult(byte[] result) {
        this.result = result;
    }
}
