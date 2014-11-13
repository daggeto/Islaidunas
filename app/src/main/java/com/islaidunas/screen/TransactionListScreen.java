package com.islaidunas.screen;

import android.os.Bundle;

import com.islaidunas.R;
import com.islaidunas.core.screen.Main;
import com.islaidunas.core.ui.ActionBarOwner;
import com.islaidunas.domain.Transaction;
import com.islaidunas.services.impl.RetrofitClient;
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
 * Layouts.createView(childContext, screen) -> view constructor for @Layout view ->
 * inject presenter -> onFinishInflate -> takeView -> presenter onLoad
 *
 *
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
        private RetrofitClient retrofitClient;

        @Inject Presenter(Flow flow, List<Transaction> transactions, ActionBarOwner actionBar, RetrofitClient retrofitClient){
            this.flow = flow;
            this.transactions = transactions;
            this.actionBar = actionBar;
            this.retrofitClient = retrofitClient;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            ActionBarOwner.Config actionBarConfig = actionBar.getConfig();

            actionBarConfig = actionBarConfig.withAction(
                    new ActionBarOwner.MenuAction(
                            "New Transaction", () ->  flow.goTo(new AddTransactionScreen()),
                            android.R.drawable.ic_menu_add)
            );

            actionBarConfig.addAction(new ActionBarOwner.MenuAction("Settings",
                    new Action0() {
                        @Override
                        public void call() {
                            retrofitClient.getContributor();
                        }
                    }
                    , android.R.drawable.ic_menu_preferences
            ));
            actionBarConfig.addAction(new ActionBarOwner.MenuAction("Categories",
                    () -> flow.goTo(new CategoryManagerScreen())
                    ,android.R.drawable.ic_dialog_dialer));

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
