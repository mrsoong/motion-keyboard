package com.example.motionkey;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
    private int mCurColour;
    ValueAnimator mColourTransition;

    public MotionKeyInteractiveElement(View view) {
        mView = view;
        mId = view.getId();
//        mLastColour = ((ColorDrawable)(view.getBackground())).getColor();
        mHeight = view.getHeight();
        mWidth = view.getWidth();
        view.getLocationInWindow(mPosition);
        //Log.d("keyboard", ((Button) view).getText().toString() +": "+" x: "+Integer.toString(mPosition[0]) + " y: " + Integer.toString(mPosition[1]));
        mColour = ((ColorDrawable)view.getBackground()).getColor();
//        Log.d("keyboard", "color: "+mColour);

        //Create a transition animator for the key color when hovering
        mColourTransition = ValueAnimator.ofObject(new ArgbEvaluator(), this.mColour, Color.parseColor("#80ddff"));
        //transition duration in milliseconds
        mColourTransition.setDuration(900);
        //animation listener
        mColourTransition.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mView.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    public String checkCursorHover(int[] position) {

        if (position[0] > mPosition[0] && position[0] < (mPosition[0] + mWidth)) {
            if (position[1] > mPosition[1] && position[1] < (mPosition[1] + mHeight)) {
                if (!keyHover) {
                    lastKeySelect = System.currentTimeMillis();
                    keyHover = true;
                    this.mCurColour = this.mColour;
                    //start color transition
                    this.mColourTransition.start();
                }
                if (keyHover && (System.currentTimeMillis() - lastKeySelect) >= 1000)  {
                    Button v = (Button)mView;
                    //Log.d("keyboardMark", (v.getText().toString() + ""));
                    keyHover = false;
                    //stop the color transition
                    this.mColourTransition.cancel();
                    return v.getText().toString();
                }
                mCursorHovering = true;
//                Log.d("KeyColorNormal", Integer.toString(this.mColour));
//                Log.d("KeyColorHighlighted1", Integer.toString(Color.parseColor("#F0F9FD")));
//                Log.d("KeyColorHighlighted2", Integer.toString(Color.parseColor("#E4F6FD")));
//                Log.d("KeyColorHighlighted3", Integer.toString(Color.parseColor("#D7F3FD")));
//                Log.d("KeyColorHighlighted4", Integer.toString(Color.parseColor("#CBF0FD")));
//                Log.d("KeyColorHighlighted5", Integer.toString(Color.parseColor("#BEEDFE")));
//                Log.d("KeyColorHighlighted6", Integer.toString(Color.parseColor("#B2E9FE")));
//                Log.d("KeyColorHighlighted7", Integer.toString(Color.parseColor("#A5E6FE")));
//                Log.d("KeyColorHighlighted8", Integer.toString(Color.parseColor("#99E3FE")));
//                Log.d("KeyColorHighlighted9", Integer.toString(Color.parseColor("#8CE0FE")));
//                Log.d("KeyColorHighlighted10", Integer.toString(Color.parseColor("#80ddff")));
//                if (this.mCurColour > Color.parseColor("#80ddff")) {
//                    this.mCurColour -= 78720;
//                    mView.setBackgroundColor(this.mCurColour);
//                } else {
//                    mView.setBackgroundColor(Color.parseColor("#80ddff"));
//                }

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
        //key is not being hovered over, stop the color transition if any started
        this.mColourTransition.cancel();
        keyHover = false;
        mCursorHovering = false;
        mView.setBackgroundColor(mColour);
        return null;
    }

    public Button checkCursorHoverButton(int[] position) {

        if (position[0] > mPosition[0] && position[0] < (mPosition[0] + mWidth)) {
            if (position[1] > mPosition[1] && position[1] < (mPosition[1] + mHeight)) {
                if (!keyHover) {
                    lastKeySelect = System.currentTimeMillis();
                    keyHover = true;
                    this.mCurColour = this.mColour;
                    //start color transition
                    this.mColourTransition.start();
                }
                if (keyHover && (System.currentTimeMillis() - lastKeySelect) >= 1000)  {
                    Button v = (Button)mView;
                    //Log.d("keyboardMark", (v.getText().toString() + ""));
                    keyHover = false;
                    //stop the color transition
                    this.mColourTransition.cancel();
                    return v;
                }
                mCursorHovering = true;
                Button v = (Button)mView;
                return null;
            }
        }
        //key is not being hovered over, stop the color transition if any started
        this.mColourTransition.cancel();
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
