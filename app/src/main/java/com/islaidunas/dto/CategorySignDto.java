package com.islaidunas.dto;

import com.islaidunas.domain.CategorySign;

/**
 * Created by daggreto on 2014.10.03.
 */
public class CategorySignDto {

    private Boolean sign;
    private String name;

    public CategorySignDto(String name) {
        this.name = name;
    }

    public CategorySignDto(CategorySign categorySign) {
        this.sign = categorySign.getBoolean();
        this.name = categorySign.toString();
    }

    @Override
    public String toString() {
        return name;
    }

    public Boolean getSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
