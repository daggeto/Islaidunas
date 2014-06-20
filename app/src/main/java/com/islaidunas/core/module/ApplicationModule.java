package com.islaidunas.core.module;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dropbox.sync.android.DbxAccountManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.islaidunas.core.GsonParcer;
import com.islaidunas.core.MainThread;
import com.islaidunas.core.model.QuoteService;
import com.islaidunas.core.ui.MainActivity;

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
@Module(includes = DatabaseModule.class, library = true)
//TODO: Investigate Scheduler and quoteService
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Provides @Singleton @MainThread Scheduler provideMainThread() {
        final Handler handler = new Handler(Looper.getMainLooper());
        return new ExecutorScheduler(new Executor() {
            @Override public void execute(Runnable command) {
                handler.post(command);
            }
        });
    }

    @Provides @Singleton Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides @Singleton Parcer<Object> provideParcer(Gson gson) {
        return new GsonParcer<Object>(gson);
    }

    @Provides @Singleton
    QuoteService provideQuoteService() {
        RestAdapter restAdapter =
                new RestAdapter.Builder().setServer("http://www.iheartquotes.com/api/v1/")
                        .setConverter(new GsonConverter(new Gson()))
                        .build();
        return restAdapter.create(QuoteService.class);
    }

    @Provides @Singleton Context provideContext(){
        return context;
    }
}
