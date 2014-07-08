package com.islaidunas.core.dbxStoreOrm.mapper;

import com.islaidunas.core.dbxStoreOrm.enums.RelationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by daggreto on 2014.06.30.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface Relation {
    String reference_id() default "";
    RelationType relationType();
}
