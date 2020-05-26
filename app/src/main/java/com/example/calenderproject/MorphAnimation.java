package com.example.calenderproject;

import android.animation.LayoutTransition;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MorphAnimation {
    private final FrameLayout parentView;
    private final View buttonContainer;
    private final ViewGroup viewsContainer;
    private boolean isPressed;
    private int initialWidth;
    private int initialGravity;
    public boolean isPressed() {
        return isPressed;
    }
    public MorphAnimation(View buttonContainer, FrameLayout parentView, ViewGroup viewsContainer) {
        this.buttonContainer = buttonContainer;
        this.parentView = parentView;
        this.viewsContainer = viewsContainer;
        LayoutTransition layoutTransition = parentView.getLayoutTransition();
        layoutTransition.setDuration(600);
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        isPressed = false;
    }
    public void morphIntoForm(String Width) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) buttonContainer.getLayoutParams();

        initialWidth = layoutParams.width;
        initialGravity = layoutParams.gravity;

        layoutParams.gravity = Gravity.CENTER;
        if (Width.equals( "WRAP_CONTENT" )){
            layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        }
        else if (Width.equals( "MATCH_PARENT" )) {
            layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        }
        buttonContainer.setLayoutParams(layoutParams);

        for (int i = 1; i < viewsContainer.getChildCount(); i++) {
            viewsContainer.getChildAt(i).setVisibility(View.VISIBLE);
        }

        isPressed = true;
    }
    public void morphIntoButton() {
        for (int i = 1; i < viewsContainer.getChildCount(); i++) {
            viewsContainer.getChildAt(i).setVisibility(View.GONE);
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) buttonContainer.getLayoutParams();
        layoutParams.gravity = initialGravity;
        layoutParams.width = initialWidth;
        buttonContainer.setLayoutParams(layoutParams);
        isPressed = false;
    }
}