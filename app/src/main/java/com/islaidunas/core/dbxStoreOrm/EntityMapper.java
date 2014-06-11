package com.islaidunas.core.dbxStoreOrm;

import com.dropbox.sync.android.DbxRecord;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by daggreto on 2014.06.11.
 */
public class EntityMapper {

    public Object getInstance(DbxRecord record, Class clazz){
        try {
            Object resultEntity = clazz.newInstance();
            Field field = clazz.getDeclaredField("id");
            //TODO: http://www.objectpartners.com/2010/08/06/how-do-annotations-work/
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DbxTable{}
}
