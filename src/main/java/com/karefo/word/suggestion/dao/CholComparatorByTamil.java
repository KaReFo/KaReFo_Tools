package com.karefo.word.suggestion.dao;

import com.karefo.word.suggestion.model.CholSearchImpl;

import java.util.Comparator;

public class  CholComparatorByTamil implements Comparator<CholSearchImpl> {

    @Override
    public int compare(CholSearchImpl o1, CholSearchImpl o2) {
        long position1 = 0;
        long position2 = 0;

        if(o1.getEnwo().toString().length() > 0 && o1.getEnpop()!=null && o1.getEnpop().toString().length() > 0)
        position1 = Long.parseLong(o1.getEnpop().toString());

        if(o2.getEnwo().toString().length() > 0 && o2.getEnpop()!=null && o2.getEnpop().toString().length() > 0)
        position2 = Long.parseLong(o2.getEnpop().toString());

        return Long.compare(position2, position1);
    }
}
