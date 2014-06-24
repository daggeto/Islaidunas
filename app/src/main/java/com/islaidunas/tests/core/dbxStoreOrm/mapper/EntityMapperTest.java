package com.islaidunas.tests.core.dbxStoreOrm.mapper;

import android.test.InstrumentationTestCase;

import com.dropbox.sync.android.DbxFields;
import com.islaidunas.core.dbxStoreOrm.mapper.DbxField;
import com.islaidunas.core.dbxStoreOrm.mapper.EntityMapper;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.tests.builder.TransactionBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by daggreto on 2014.06.15.
 */
public class EntityMapperTest extends InstrumentationTestCase{

    public static final String TEST_TX_NAME = "TestTx";
    public static final BigDecimal AMOUNT = new BigDecimal(10);
    public static final Date DATE = new Date();
    public static final String ID = "1";
    private EntityMapper testObject;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testObject = new EntityMapper();
    }


    public void testParseEntity() throws  Exception{
        //TODO: create entity specially for testing with all of parse types
        Transaction transaction = new TransactionBuilder()
                .withTitle(TEST_TX_NAME)
                .withAmount(AMOUNT)
                .withDate(DATE)
                .withCategory(new Category())
                .build();

        DbxFields result = testObject.parseEntity(transaction);

        assertEquals(result.getString("title"), TEST_TX_NAME);
        assertEquals(result.getDouble("amount"), AMOUNT.doubleValue());
        assertEquals(result.getDate("date"), DATE);

    }

    public void testGetId() throws NoSuchFieldException, IllegalAccessException {
        Transaction transaction = new TransactionBuilder(ID)
                .withTitle(TEST_TX_NAME)
                .withAmount(AMOUNT)
                .withDate(DATE)
                .withCategory(new Category())
                .build();

        String result = testObject.getId(transaction);
    }

}
