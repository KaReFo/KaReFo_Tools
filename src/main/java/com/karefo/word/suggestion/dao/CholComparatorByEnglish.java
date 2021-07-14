package com.karefo.word.suggestion.dao;

import com.karefo.word.suggestion.model.CholSearchImpl;

import java.util.Comparator;

public class CholComparatorByEnglish  implements Comparator<CholSearchImpl> {

    @Override
    public int compare(CholSearchImpl o1, CholSearchImpl o2) {
        long position1 = 0;
        long position2 = 0;

        if(o1.getTawo().toString().length() > 0 && o1.getTapop()!=null && o1.getTapop().toString().length() > 0)
            position1 = Long.parseLong(o1.getTapop().toString());

        if(o2.getTawo().toString().length() > 0 && o2.getTapop()!=null && o2.getTapop().toString().length() > 0)
            position2 = Long.parseLong(o2.getTapop().toString());

        return Long.compare(position2, position1);
    }
}
