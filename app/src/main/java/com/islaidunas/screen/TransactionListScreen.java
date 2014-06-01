package com.islaidunas.screen;

import android.os.Bundle;

import com.islaidunas.R;
import com.islaidunas.core.screen.Main;
import com.islaidunas.core.ui.ActionBarOwner;
import com.islaidunas.domain.Transaction;
import com.islaidunas.services.impl.dao.TransactionJpaDao;
import com.islaidunas.ui.view.TransactionListView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import rx.util.functions.Action0;

/**
 * Created by daggreto on 2014.05.13.
 */
@Layout(R.layout.transaction_list)
public class TransactionListScreen implements Blueprint {
    //TODO: why lagging on this screen load?
    @dagger.Module(injects = TransactionListView.class, addsTo = Main.Module.class)
    public class Module {
        @Provides List<Transaction> provideTransactions(TransactionJpaDao transactionJpaDao){
            return transactionJpaDao.findAll();
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<TransactionListView> {
        private Flow flow;
        private List<Transaction> transactions;
        private ActionBarOwner actionBar;

        @Inject Presenter(Flow flow, List<Transaction> transactions, ActionBarOwner actionBar){
            this.flow = flow;
            this.transactions = transactions;
            this.actionBar = actionBar;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            ActionBarOwner.Config actionBarConfig = actionBar.getConfig();

            actionBarConfig = actionBarConfig.withAction(
                    new ActionBarOwner.MenuAction("New Transaction",
                            new Action0() {
                                @Override public void call() {
                                    flow.goTo(new AddTransactionScreen());
                                }
                            }
                            , android.R.drawable.ic_menu_add
            ));

            actionBar.setConfig(actionBarConfig);

        }

        public List<Transaction> getTransactions(){
            return transactions;
        }

        public void goToTransaction(Integer id){
            flow.goTo(new AddTransactionScreen(id));
        }
    }

    @Override public String getMortarScopeName() {
        return "Transactions";
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }

}
