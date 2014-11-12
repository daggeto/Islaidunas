package com.islaidunas.core.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.islaidunas.IslaidunasApplication;
import com.islaidunas.R;
import com.islaidunas.core.screen.Main;
import com.islaidunas.core.view.MainView;

import java.util.List;

import javax.inject.Inject;

import flow.Flow;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarScope;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_LAUNCHER;
import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;

public class MainActivity extends ActionBarActivity implements ActionBarOwner.View{
    private MortarActivityScope activityScope;
    private List<ActionBarOwner.MenuAction> actionBarMenuAction;

    private Flow mainFlow;
    @Inject ActionBarOwner actionBarOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IslaidunasApplication.activityContext = this;

        if (isWrongInstance()) {
            finish();
            return;
        }

        MortarScope parentScope = Mortar.getScope(getApplication());
        activityScope = Mortar.requireActivityScope(parentScope, new Main());
        Mortar.inject(this, this);

        activityScope.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        MainView mainView = (MainView) findViewById(R.id.container);
        mainFlow = mainView.getFlow();

        actionBarOwner.takeView(this);
    }

    @Override public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return activityScope;
        }
        return super.getSystemService(name);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityScope.onSaveInstanceState(outState);
    }

    @Override public void onBackPressed() {
        //TODO: how register onBackPressed for every screen
        if (!mainFlow.goBack()) super.onBackPressed();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return mainFlow.goUp();
        }

        return super.onOptionsItemSelected(item);
    }

    /** Configure the action bar menu as required by {@link ActionBarOwner.View}. */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        if (actionBarMenuAction != null) {
            for(final ActionBarOwner.MenuAction menuAction: actionBarMenuAction)
                menu.add(menuAction.title)
                    .setShowAsActionFlags(SHOW_AS_ACTION_ALWAYS)
                    .setOnMenuItemClickListener(menuItem -> {
                        menuAction.action.call();
                        return true;
                    })
                    .setIcon(menuAction.image);
        }
        return true;
    }

    private boolean isWrongInstance() {
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            boolean isMainAction = intent.getAction() != null && intent.getAction().equals(ACTION_MAIN);
            return intent.hasCategory(CATEGORY_LAUNCHER) && isMainAction;
        }
        return false;
    }

    @Override
    public void setShowHomeEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(enabled);
    }

    @Override
    public void setUpButtonEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(enabled);
        actionBar.setHomeButtonEnabled(enabled);
    }

    @Override
    public void setMenu(List<ActionBarOwner.MenuAction> action) {
        if (action != actionBarMenuAction) {
            actionBarMenuAction = action;
            invalidateOptionsMenu();
        }
    }

    @Override
    public Context getMortarContext() {
        return this;
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        actionBarOwner.dropView(this);

        // activityScope may be null in case isWrongInstance() returned true in onCreate()
        if (isFinishing() && activityScope != null) {
            activityScope.destroy();
            activityScope = null;
        }
    }

}
