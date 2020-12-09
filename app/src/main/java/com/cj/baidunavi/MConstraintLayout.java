package com.cj.baidunavi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by 大头 on 2020/5/6.
 */

public class MConstraintLayout extends ConstraintLayout {
    public MConstraintLayout(Context context) {
        super(context);
    }

    public MConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        View lastChild=getChildAt(getChildCount()-1);
        if(lastChild.getMeasuredHeight()+lastChild.getTop()>=getMeasuredHeight()){
            lastChild.layout(lastChild.getLeft(),lastChild.getTop(),lastChild.getRight(),
                    getMeasuredHeight()-100);
        }
    }
}
