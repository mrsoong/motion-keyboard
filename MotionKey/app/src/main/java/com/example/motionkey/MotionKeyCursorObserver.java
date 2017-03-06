package com.example.motionkey;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2017 MotionKey
 */


/**
 * Relays the cursor position to all interactive elements
 */

public class MotionKeyCursorObserver {

    MotionKeyKeyboardView mMotionKeyView;
    List<MotionKeyInteractiveElement> mAllInteractiveElements = new ArrayList<MotionKeyInteractiveElement>();
    private boolean mAllElementsAdded = false;
    private int[] mCursorPosition;


    public MotionKeyCursorObserver(MotionKeyKeyboardView motionkeykeyboardview) {
        mMotionKeyView = motionkeykeyboardview;
        findElements(mMotionKeyView.findViewById(R.id.keyboard_parent_layout));
        mAllElementsAdded = true;
    }


    //recursively add all the button views to the list of interactive elements
    public void findElements(View parent_view){
        for (int i=0; i < ((ViewGroup)parent_view).getChildCount(); i++) {
            View tmp_child = ((ViewGroup) parent_view).getChildAt(i);
            if (tmp_child instanceof Button) {
                mAllInteractiveElements.add(new MotionKeyInteractiveElement(tmp_child));
            }
            if (!(tmp_child instanceof Button || tmp_child instanceof TextView || tmp_child instanceof ImageView)) {
                if (((ViewGroup) tmp_child).getChildCount() > 0) {
                    findElements(tmp_child);
                }
            }
        }
    }

    public String updateCursorPosition(int[] position) {
        for (MotionKeyInteractiveElement element : mAllInteractiveElements) {
            String tmp = element.checkCursorHover(position);
            if(tmp != null){
                return tmp;
            }
        }
        return null;
    }
}

