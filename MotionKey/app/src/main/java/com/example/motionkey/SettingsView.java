package com.example.motionkey;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class SettingsView extends RelativeLayout {
    private SettingsCursorObserver mSettingsObserver;
    private boolean mElementFound = false;

    public SettingsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.d("Mark1", !mElementFound + "");
        if (!mElementFound) {
            mSettingsObserver = new SettingsCursorObserver(this);
            mElementFound = true;
        }
    }

    public SettingsCursorObserver getMotionKeyElements() {
        return mSettingsObserver;
    }

    public boolean isMotionKeyKeyboardElementsFound() {
        return mElementFound;
    }
}
