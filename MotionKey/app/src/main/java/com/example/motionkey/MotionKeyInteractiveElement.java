package com.example.motionkey;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

/**
 * Copyright (C) 2017 MotionKey
 * File Created on 3/4/17
 */


/**
 * A view affected by the cursor hovering over it
 */

public class MotionKeyInteractiveElement {

    private View mView;
    private int mId;
//    private int mLastColour;
    private boolean mCursorHovering = false;
    private int mHeight;
    private int mWidth;
    private int[] mPosition = new int[2];
    private int mColour;

    public MotionKeyInteractiveElement(View view) {
        mView = view;
        mId = view.getId();
//        mLastColour = ((ColorDrawable)(view.getBackground())).getColor();
        mHeight = view.getHeight();
        mWidth = view.getWidth();
        view.getLocationInWindow(mPosition);
        mColour = ((ColorDrawable)view.getBackground()).getColor();
//        Log.d("keyboard", "color: "+mColour);
    }

    public void checkCursorHover(int[] position) {
        if (position[0] > mPosition[0] && position[0] < (mPosition[0] + mWidth)) {
            if (position[1] > mPosition[1] && position[1] < (mPosition[1] + mHeight)) {
                mCursorHovering = true;
                mView.setBackgroundColor(Color.parseColor("#80ddff"));
                return;
            }
        }
//        if (mView.getTag() != null) {
//            if (((String)mView.getTag()).equals("thedkey")) {
//                Log.d("keyboard", "d: "+" x: "+mPosition[0] + " y: "+mPosition[1]);
//                Log.d("keyboard", "cursor: "+" x: "+position[0] + " y: "+position[1]);
//            }
//        }

        mCursorHovering = false;
        mView.setBackgroundColor(mColour);
    }
}
