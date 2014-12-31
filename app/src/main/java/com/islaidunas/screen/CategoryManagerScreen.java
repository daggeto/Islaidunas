package com.islaidunas.screen;

import android.os.Bundle;

import com.islaidunas.R;
import com.islaidunas.core.screen.Main;
import com.islaidunas.core.ui.ActionBarOwner;
import com.islaidunas.domain.Bucket;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.CategorySign;
import com.islaidunas.dto.CategorySignDto;
import com.islaidunas.services.impl.dao.BucketJpaDao;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.islaidunas.ui.view.CategoryManagerView;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

@Layout(R.layout.category_manager)
public class CategoryManagerScreen implements Blueprint, HasParent<TransactionListScreen> {

    @dagger.Module(injects = CategoryManagerView.class, addsTo = Main.Module.class)
    public class Module {
    }
    @Singleton
    public static class Presenter extends ViewPresenter<CategoryManagerView>{

        private Flow flow;
        private CategoryJpaDao categoryJpaDao;
        private BucketJpaDao bucketJpaDao;

        @Inject ActionBarOwner actionBar;

        @Inject Presenter(Flow flow, CategoryJpaDao categoryJpaDao, BucketJpaDao bucketJpaDao){
            this.flow = flow;
            this.categoryJpaDao = categoryJpaDao;
            this.bucketJpaDao = bucketJpaDao;
        }

        @Override protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            ActionBarOwner.Config actionBarConfig = actionBar.getConfig();
            actionBarConfig.setTitleEnabled(false);
            actionBar.setConfig(actionBarConfig);
        }

        public Observable<List<Category>> getCategories(){
            return Observable.create((Observable.OnSubscribe<List<Category>>) subscriber -> {
                subscriber.onNext(categoryJpaDao.findAll());
                subscriber.onCompleted();
            });
        }

        public List<Bucket> findAllBuckets(){
            return bucketJpaDao.findAll();
        }

        public List<CategorySignDto> getSigns(){
            return Arrays.asList(
                    new CategorySignDto(CategorySign.positive),
                    new CategorySignDto(CategorySign.negative));
        }
    }

    @Override
    public String getMortarScopeName() {
        return "Category Manager";
    }

    @Override
    public Object getDaggerModule() {
        return new Module();
    }

    @Override
    public TransactionListScreen getParent() {
        return new TransactionListScreen();
    }
}
