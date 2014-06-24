package com.islaidunas.screen;

import android.os.Bundle;
import android.view.View;

import com.islaidunas.R;
import com.islaidunas.core.screen.Main;
import com.islaidunas.core.ui.ActionBarOwner;
import com.islaidunas.dao.dbx.TransactionJpaDao;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.dao.CategoryJpaDao;
import com.islaidunas.ui.view.AddTransactionView;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import rx.util.functions.Action0;

/**
 * Created by daggreto on 2014.05.15.
 */
@Layout(R.layout.add_transaction)
public class AddTransactionScreen implements Blueprint, HasParent<TransactionListScreen> {
    private final String transactionId;

    public AddTransactionScreen(){
        this(null);
    }

    public AddTransactionScreen(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public TransactionListScreen getParent() {
        return new TransactionListScreen();
    }

    @dagger.Module(injects = AddTransactionView.class, addsTo = Main.Module.class)
    public class Module{
        @Provides Transaction provideTransaction(TransactionJpaDao transactionJpaDao){
            if(transactionId == null){
                return new Transaction();
            }
            return transactionJpaDao.findById(transactionId);
        }

        @Provides List<Category> provideCategories(CategoryJpaDao categoryJpaDao){
            return categoryJpaDao.findAll();
        }

    }

    @Singleton
    public static class Presenter extends ViewPresenter<AddTransactionView>{
        private Flow flow;
        private ActionBarOwner actionBar;
        private Transaction transaction;
        private List<Category> categories;
        private TransactionJpaDao transactionJpaDao;
        private Validator validator;
        private boolean isFormValid = false;

        @Inject Presenter(Flow flow, Transaction transaction, List<Category> categories, ActionBarOwner actionBar, TransactionJpaDao transactionJpaDao){
            this.flow = flow;
            this.transaction = transaction;
            this.categories = categories;
            this.actionBar = actionBar;
            this.transactionJpaDao = transactionJpaDao;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            ActionBarOwner.Config actionBarConfig = actionBar.getConfig();

            actionBarConfig = actionBarConfig.withAction(
                    new ActionBarOwner.MenuAction("Save",
                            new Action0() {
                                @Override
                                public void call() {
                                    validator.validate();
                                    if(isFormValid){
                                        saveTransaction();
                                        goBack();
                                    }
                                }
                            }, android.R.drawable.ic_menu_save
                    ));
            //TODO: exception here
            if(transaction.getId() != null){
                actionBarConfig.addAction(new ActionBarOwner.MenuAction("Delete",
                        new Action0() {
                            @Override
                            public void call() {
                                deleteTransaction();
                                goBack();
                            }
                        }, android.R.drawable.ic_menu_delete
                ));
            }

            actionBar.setConfig(actionBarConfig);
        }

        public void saveTransaction(){
            transactionJpaDao.save(transaction);
        }

        public void deleteTransaction(){
            transactionJpaDao.delete(transaction);
        }

        public Transaction getTransaction(){
            return transaction;
        }

        public List<Category> getCategories(){
            return categories;
        }

        public void initValidator(View view){
            if(view instanceof Validator.ValidationListener) {
                validator = new Validator(view);
                validator.setValidationListener((Validator.ValidationListener) view);
            }
        }

        public void setFormValid(boolean valid){
            isFormValid = valid;
        }

        public void goBack(){
            flow.goBack();
        }
    }

    @Override public String getMortarScopeName() {
        return "Transaction";
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }
}
