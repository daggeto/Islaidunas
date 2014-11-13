package com.islaidunas.core.screen;

import android.content.Context;
import android.content.res.Resources;

import com.islaidunas.core.FlowOwner;
import com.islaidunas.core.module.ActionBarModule;
import com.islaidunas.core.module.ApplicationModule;
import com.islaidunas.core.ui.ActionBarOwner;
import com.islaidunas.core.view.MainView;
import com.islaidunas.screen.TransactionListScreen;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Parcer;
import mortar.Blueprint;

/**
 * Created by daggreto on 2014.05.19.
 */
public class Main implements Blueprint {
    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new Module();
    }

    @dagger.Module(
            includes = ActionBarModule.class,
            injects = MainView.class,
            addsTo = ApplicationModule.class, //
            library = true //
    )
    public static class Module {
        @Provides Flow provideFlow(Presenter presenter) {
            return presenter.getFlow();
        }
    }

    @Singleton public static class Presenter extends FlowOwner<Blueprint, MainView> {
        private final ActionBarOwner actionBarOwner;
        private Resources resources;

        @Inject Presenter(Parcer<Object> flowParcer, ActionBarOwner actionBarOwner, Context context) {
            super(flowParcer);
            this.actionBarOwner = actionBarOwner;
            this.resources = context.getResources();
        }

        @Override public void showScreen(Blueprint newScreen, Flow.Direction direction) {
            boolean hasUp = newScreen instanceof HasParent;
            String title = newScreen.getMortarScopeName();
            actionBarOwner.setConfig(new ActionBarOwner.Config(true, hasUp, title, null));

            super.showScreen(newScreen, direction);
        }

        @Override protected Blueprint getFirstScreen() {
            return new TransactionListScreen();
        }

    }
}
