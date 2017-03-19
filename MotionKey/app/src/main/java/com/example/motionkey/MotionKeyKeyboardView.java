/*
 * Copyright (C) 2017 MotionKey
 *
 */


package com.example.motionkey;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


/**
 * The main input method view layout. Holds everything together.
 */
public class MotionKeyKeyboardView extends RelativeLayout {

    private MotionKeyCursorObserver mMotionKeyCursorObserver;
    private boolean mMotionKeyInteractiveElementsFound = false;

    public MotionKeyKeyboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mMotionKeyInteractiveElementsFound) {
            mMotionKeyCursorObserver = new MotionKeyCursorObserver(this);
            mMotionKeyInteractiveElementsFound = true;
        }
    }

    public MotionKeyCursorObserver getMotionKeyElements() {
        return mMotionKeyCursorObserver;
    }

    public boolean isMotionKeyKeyboardElementsFound() {
        return mMotionKeyInteractiveElementsFound;
    }

//    public void resetMotionKeyKeyboardElementsFound() {
//        this.mMotionKeyInteractiveElementsFound = false;
//    }

}
