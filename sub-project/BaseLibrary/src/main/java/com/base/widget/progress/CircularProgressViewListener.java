package com.base.widget.progress;

/**
 * Created by jzy on 2017/6/12.
 */

public interface CircularProgressViewListener {
    void onProgressUpdate(float currentProgress);

    void onProgressUpdateEnd(float currentProgress);

    void onAnimationReset();

    void onModeChanged(boolean isIndeterminate);
}
