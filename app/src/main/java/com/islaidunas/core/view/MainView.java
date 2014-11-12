package com.islaidunas.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.islaidunas.core.CanShowScreen;
import com.islaidunas.core.ScreenConductor;
import com.islaidunas.core.screen.Main;

import javax.inject.Inject;

import flow.Flow;
import mortar.Blueprint;
import mortar.Mortar;

/**
 * Created by daggreto on 2014.05.19.
 */
public class MainView extends FrameLayout implements CanShowScreen<Blueprint> {
    @Inject Main.Presenter presenter;
    private final ScreenConductor<Blueprint> screenMaestro;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
        screenMaestro = new ScreenConductor<Blueprint>(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.takeView(this);
    }

    public Flow getFlow() {
        return presenter.getFlow();
    }

    /**
     * takeView -> FlowOwner#onLoad -> showScreen -> showScreen for view that attached to presenter
     */
    @Override public void showScreen(Blueprint screen, Flow.Direction direction) {
        screenMaestro.showScreen(screen, direction);
    }
}
