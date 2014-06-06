/*
 * Copyright 2013 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.islaidunas.core.ui;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mortar.Mortar;
import mortar.MortarScope;
import mortar.Presenter;
import rx.util.functions.Action0;

/** Allows shared configuration of the Android ActionBar. */
public class ActionBarOwner extends Presenter<ActionBarOwner.View> {
    public interface View {
        void setShowHomeEnabled(boolean enabled);

        void setUpButtonEnabled(boolean enabled);

        void setTitle(CharSequence title);

        void setMenu(List<MenuAction> action);

        Context getMortarContext();
    }

    public static class Config {
        public final boolean showHomeEnabled;
        public final boolean upButtonEnabled;
        public final CharSequence title;
        public final List<MenuAction> actions;

        public Config(boolean showHomeEnabled, boolean upButtonEnabled, CharSequence title,
                      List<MenuAction> actions) {
            this.showHomeEnabled = showHomeEnabled;
            this.upButtonEnabled = upButtonEnabled;
            this.title = title;
            if(actions == null){
                actions = new ArrayList<MenuAction>();
            }
            this.actions = actions;
        }

        public Config withAction(MenuAction action) {
            List<MenuAction> newInstance = new ArrayList<MenuAction>(actions);
            newInstance.add(action);
            return new Config(showHomeEnabled, upButtonEnabled, title, newInstance);
        }

        public Config addAction(MenuAction action){
            actions.add(action);
            return this;
        }
    }

    public static class MenuAction {
        public final CharSequence title;
        public final Action0 action;
        public int image;

        public MenuAction(CharSequence title, Action0 action) {
            this.title = title;
            this.action = action;
        }

        public MenuAction(CharSequence title, Action0 action, int image){
            this(title, action);
            this.image = image;
        }
    }

    private Config config;

    public ActionBarOwner() {
    }

    @Override public void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        if (config != null) update();
    }

    public void setConfig(Config config) {
        this.config = config;
        update();
    }

    public Config getConfig() {
        return config;
    }

    @Override protected MortarScope extractScope(View view) {
        return Mortar.getScope(view.getMortarContext());
    }

    private void update() {
        View view = getView();
        if (view == null) return;

        view.setShowHomeEnabled(config.showHomeEnabled);
        view.setUpButtonEnabled(config.upButtonEnabled);
        view.setTitle(config.title);
        view.setMenu(config.actions);
    }
}

