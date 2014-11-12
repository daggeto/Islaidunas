package com.islaidunas.domain;

import com.islaidunas.IslaidunasApplication;
import com.islaidunas.R;

/**
 * Created by daggreto on 2014.10.03.
 */
public enum CategorySign {
    positive(R.string.positive), negative(R.string.negative);

    private final int id;
    private String name;

    private CategorySign(int id) {
        this.id = id;
        this.name = IslaidunasApplication.getContext().getString(id);
    }

    @Override
    public String toString() {
        return name;
    }

    public static CategorySign getCategorySign(boolean sign){
        return sign ? positive : negative;
    }

    public Boolean getBoolean(){
        if(id == R.string.positive){
           return true;
        }

        return  false;
    }

}
