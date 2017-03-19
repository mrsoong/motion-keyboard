package com.example.motionkey;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

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
    private long lastKeySelect = 0;
    private boolean keyHover = false;

    public MotionKeyInteractiveElement(View view) {
        mView = view;
        mId = view.getId();
//        mLastColour = ((ColorDrawable)(view.getBackground())).getColor();
        mHeight = view.getHeight();
        mWidth = view.getWidth();
        view.getLocationInWindow(mPosition);
        Log.d("keyboard", ((Button) view).getText().toString() +": "+" x: "+Integer.toString(mPosition[0]) + " y: " + Integer.toString(mPosition[1]));
        mColour = ((ColorDrawable)view.getBackground()).getColor();
//        Log.d("keyboard", "color: "+mColour);
    }

    public String checkCursorHover(int[] position) {

        if (position[0] > mPosition[0] && position[0] < (mPosition[0] + mWidth)) {
            if (position[1] > mPosition[1] && position[1] < (mPosition[1] + mHeight)) {
                if (!keyHover) {
                    lastKeySelect = System.currentTimeMillis();
                    keyHover = true;
                }
                if (keyHover && (System.currentTimeMillis() - lastKeySelect) >= 1000)  {
                    Button v = (Button)mView;
                    //Log.d("keyboardMark", (v.getText().toString() + ""));
                    keyHover = false;
                    return v.getText().toString();
                }
                mCursorHovering = true;
                mView.setBackgroundColor(Color.parseColor("#80ddff"));
                Button v = (Button)mView;
//                Log.d("keyboardMark", (v.getText().toString() + ""));
//                Log.d("keyboardMark", System.currentTimeMillis() + "");
                return null;
            }
        }

//        if (mView.getTag() != null) {
//            if (((String)mView.getTag()).equals("thedkey")) {
//                Log.d("keyboard", "d: "+" x: "+mPosition[0] + " y: "+mPosition[1]);
//                Log.d("keyboard", "cursor: "+" x: "+position[0] + " y: "+position[1]);
//            }
//        }
        keyHover = false;
        mCursorHovering = false;
        mView.setBackgroundColor(mColour);
        return null;
    }
    public Integer getKeyID(HashMap<int[], Integer> coord, int[] cursor){
        Integer value;
        ArrayList<int[]> keyPositions = new ArrayList<int[]>();
        for (int[] x : coord.keySet()){
            keyPositions.add(x);
        }
        for (int i = 0; i < keyPositions.size(); i++){
            if (30*(Math.floor(cursor[0])) == coord.get(keyPositions.get(i)[0]) && 30*(Math.floor(cursor[1])) == coord.get(keyPositions.get(i)[1])){
                return coord.get(cursor);
            }
        }
        return null;
    }
}
