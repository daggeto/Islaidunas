package com.islaidunas.core.dbxStoreOrm.mapper;

import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.islaidunas.core.dbxStoreOrm.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by daggreto on 2014.06.11.
 */
//TODO: Check if id exists in entity
public class EntityMapper {

    private static final String TAG = EntityMapper.class.getSimpleName();

    private EntityParseHelper parseHelper = new EntityParseHelper();

    public DbxFields parseEntity(Object entity){
        if(!isDbxTable(entity)){
            //TODO:[Exceptions] Create exception "Entity is not declared as Dbx table with @DbxTable annotation"
            return null;
        }

        List<Field> fields = getDbxFields(entity);
        DbxFields record = new DbxFields();
        for(Field field : fields){
            try {
                field.setAccessible(true);
                Method setter = parseHelper.getSetter(field.getType());

                if(setter != null){
                    setter.invoke(parseHelper, field.getName(), field.get(entity), record);
                }
            } catch (IllegalAccessException e) {
                Logger.error(TAG, e.toString());
            }catch (InvocationTargetException e) {
                Logger.error(TAG, e.toString());
            }
        }

        return record;
    }

    private List<Field> getDbxFields(Object entity) {
        List<Field> result =  new ArrayList<Field>();

        for(Field field : Arrays.asList(entity.getClass().getDeclaredFields())){
            if(isDbxField(field)) result.add(field);
        }

        return result;
    }

    public String getId(Object entity) throws NoSuchFieldException, IllegalAccessException{
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return String.valueOf(idField.get(entity));
        } catch (NoSuchFieldException e) {
            //TODO:[Exceptions] Create Excetion "No id field inside entity"
            throw new NoSuchFieldException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException(e.getMessage());
        }
    }
    //TODO: throw record null assertion exception
    public Object getInstanceOf(Class clazz, DbxRecord record)
            throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        try {
            Object resultEntity = clazz.newInstance();
            List<Field> fields = getDbxFields(resultEntity);

            for(Field field : fields){
                Method getter = parseHelper.getGetter(field.getType());
                if(getter == null){
                    Logger.warn(TAG, "No such getter for type in Mapper: " + field.getType());
                    continue;
                }
                Object value = getter.invoke(parseHelper, field.getName(), record);
                field.setAccessible(true);
                field.set(resultEntity, value);
            }

            setId(clazz, record, resultEntity);

            return resultEntity;

        } catch (InvocationTargetException e) {
            //TODO:[Exception] "Mapper cannot invoke getter method"
            throw new InvocationTargetException(e, e.getMessage());
        }catch (InstantiationException e) {
            //TODO:[Exception] "Cannot create instance of class ..."
            throw new InstantiationException(e.getMessage());
        } catch (IllegalAccessException e) {
            //TODO:[Exception] "Cannot access field ... from class ..."
            throw new IllegalAccessException(e.getMessage());
        } catch (NoSuchFieldException e){
            throw new NoSuchFieldException(e.getMessage());
        }
    }

    private void setId(Class clazz, DbxRecord record, Object resultEntity) throws NoSuchFieldException, IllegalAccessException {
        Field idField = getIdField(clazz);

        if(idField == null){
            Logger.error(TAG, "No id field in: " + clazz.toString());
            throw new IllegalArgumentException("No id field in: " + clazz.toString());
        }

        idField.setAccessible(true);
        idField.set(resultEntity, record.getId());
    }

    private Field getIdField(Class clazz) throws NoSuchFieldException {
        Field idField = null;

        for(Field field : Arrays.asList(clazz.getDeclaredFields())){
            if(field.isAnnotationPresent(DbxId.class)){
                idField = field;
            }
        }

        return idField;
    }

    private boolean isDbxTable(Object entity){
        return entity.getClass().isAnnotationPresent(DbxTable.class);
    }

    private boolean isDbxField(Field field){
        return field.isAnnotationPresent(DbxField.class);
    }

    public class EntityParseHelper {

        private final String SETTER_METHOD_PREFIX = "set";
        private final String GETTER_METHOD_PREFIX = "get";

        public void setString(String name,  Object field, DbxFields fields){
            fields.set(name, (String) field);
        }

        public void setBigDecimal(String name, Object bigDecimal, DbxFields fields){
            fields.set(name, ((BigDecimal) bigDecimal).doubleValue());
        }

        public void setDate(String name, Object date, DbxFields fields){
            fields.set(name, (Date) date);
        }

        public String getString(String name, DbxRecord record){
            return record.getString(name);
        }

        public BigDecimal getBigDecimal(String name, DbxRecord record){
            return new BigDecimal(record.getDouble(name));
        }

        public Date getDate(String name, DbxRecord record){
            return record.getDate(name);
        }

        public Method getSetter(Class type){
            try {
                return this.getClass().getMethod(SETTER_METHOD_PREFIX + type.getSimpleName(), String.class, Object.class, DbxFields.class);
            } catch (NoSuchMethodException e) {
                Logger.error(TAG, e.toString());
                return null;
            }
        }

        public Method getGetter(Class type){
            try {
                return this.getClass().getMethod(GETTER_METHOD_PREFIX + type.getSimpleName(), String.class, DbxRecord.class);
            } catch (NoSuchMethodException e) {
                Logger.error(TAG, e.toString());
                return null;
            }
        }
    }


}
