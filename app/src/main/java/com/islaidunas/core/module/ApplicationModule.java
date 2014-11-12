package com.islaidunas.core.module;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.islaidunas.core.GsonParcer;
import com.islaidunas.core.MainThread;
import com.islaidunas.core.model.QuoteService;
import com.islaidunas.services.impl.RetrofitClient;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import flow.Parcer;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Scheduler;
import rx.concurrency.ExecutorScheduler;

/**
 * Created by daggreto on 2014.05.19.
 */
@Module(includes = DatabaseModule.class, library = true, complete = true)
//TODO: Investigate Scheduler and quoteService
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Provides @Singleton
    RetrofitClient getRetrofitClient(){
        return new RetrofitClient();
    }

    @Provides @Singleton Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides @Singleton Parcer<Object> provideParcer(Gson gson) {
        return new GsonParcer<Object>(gson);
    }

    @Provides @Singleton Context provideContext(){
        return context;
    }
}
