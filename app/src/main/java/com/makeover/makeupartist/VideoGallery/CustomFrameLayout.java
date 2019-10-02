package com.makeover.makeupartist.VideoGallery;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CustomFrameLayout extends FrameLayout {

    private boolean actionDownReceived = false;

    public CustomFrameLayout(Context context) {
        super(context);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                actionDownReceived = true;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // No longer a click, probably a gesture.
                actionDownReceived = false;
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (actionDownReceived) {
                    performClick();
                }
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
