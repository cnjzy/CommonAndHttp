package com.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.library.R;
import com.library.util.res.ResHelper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewUtils {
    /**
     * 设置TextView右侧图标
     *
     * @param context
     * @param view
     * @param right
     */
    public static void setViewRightDrawable(Context context, TextView view, int right) {
        setViewDrawable(context, view, 0, 0, right, 0);
    }

    /**
     * 设置TextView右侧图标
     *
     * @param context
     * @param view
     * @param left
     */
    public static void setViewLeftDrawable(Context context, TextView view, int left) {
        setViewDrawable(context, view, left, 0, 0, 0);
    }

    /**
     * 设置TextView图标
     *
     * @param context
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setViewDrawable(Context context, TextView view, int left,
                                       int top, int right, int bottom) {
        Drawable leftDrawable = null, topDrawable = null, rightDrawable = null, bottomDrawable = null;
        if (left > 0) {
            leftDrawable = context.getResources().getDrawable(left);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                    leftDrawable.getMinimumHeight());
        }
        if (top > 0) {
            topDrawable = context.getResources().getDrawable(top);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
                    topDrawable.getMinimumHeight());
        }
        if (right > 0) {
            rightDrawable = context.getResources().getDrawable(right);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                    rightDrawable.getMinimumHeight());
        }
        if (bottom > 0) {
            bottomDrawable = context.getResources().getDrawable(bottom);
            bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(),
                    bottomDrawable.getMinimumHeight());
        }
        view.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable,
                bottomDrawable);
    }

    /**
     * 执行动画
     *
     * @param view
     */
    public static void startBackgroundAnimation(View view) {
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.start();
    }

    /**
     * 执行动画
     *
     * @param view
     */
    public static void startBackgroundAnimation(View view, int animo) {
        view.setBackgroundResource(animo);
        startBackgroundAnimation(view);
    }

    /**
     * 获取listview总高度
     *
     * @param listView
     */
    public static int getListViewHeightBasedOnChildren(ListView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        // 固定列宽，有多少列
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i++) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        return totalHeight;
    }

    /**
     * 根据listview item 高度设置listview背景颜色
     *
     * @param listView
     * @param bgRsid   不满足高度背景色
     */
    public static void setListViewBackgrondByListViewHeight(final ListView listView, final int bgRsid) {
        if (listView != null) {
            listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    int itemHeight = 0;
                    for (int i = 0; i < listView.getChildCount(); i++) {
                        itemHeight += listView.getChildAt(i).getHeight();
                    }
                    int listViewHeight = listView.getHeight();

                    if (itemHeight < listViewHeight) {
                        listView.setBackgroundColor(ResHelper.getInstance().getColor(bgRsid));
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            listView.setBackground(null);
                        } else {
                            listView.setBackgroundDrawable(null);
                        }

                    }
                }
            });

        }
    }

    /**
     * 动态获取gridview高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(GridView listView, int colNum) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += colNum) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
//        // 设置margin
//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    public static void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    // 获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    // 计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    // 键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 图片上画字 *
     */
    public static Bitmap drawTextAtBitmap(Bitmap bitmap, String text, int textSize, int textColor, int padding) {
        // 获取文字宽度
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        int textWidth = getTextWidth(textPaint, text);

        // 获取文字高度
        Rect rect = new Rect();
        textPaint.getTextBounds("哈哈", 0, 1, rect);
        int textHeight = rect.height();

        // 创建新的图片
        int x = padding + bitmap.getWidth() + textWidth + padding;
        int y = bitmap.getHeight() > textHeight ? bitmap.getHeight() : textHeight;
        Bitmap newbit = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newbit);

        // 开始写入文字
        Paint paint = new Paint(); // 在原始位置0，0插入原图
        canvas.drawBitmap(bitmap, padding, 0, paint);

        paint.setColor(textColor);
        paint.setTextSize(textSize); // 在原图指定位置写上字
        canvas.drawText(text, bitmap.getWidth() + padding + padding, (newbit.getHeight() + textHeight) / 2, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);

        // 存储
        canvas.restore();
        return newbit;
    }


    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 替换指定文字内容颜色
     *
     * @param textView
     * @param span
     * @param color
     */
    public static void changeSpanTextColor(TextView textView, String span, int color) {
        if (textView == null)
            return;
        String content = textView.getText().toString();
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(span) || content.indexOf(span) == -1) {
            return;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText());
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        builder.setSpan(colorSpan, content.indexOf(span), content.indexOf(span) + span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    public static void replaceCommentMentioned(TextView textView) {
        if (textView == null)
            return;
        String content = textView.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;

        }
        Pattern p = Pattern.compile("\\s\\[.+\\]\\s");
        Matcher m = p.matcher(content);
        ArrayList<String> tags = new ArrayList<String>();
        while (m.find()) {
            String str = m.group();
            String temp = str.replace("[", "").replace("]", "");
            content = content.replace(str, temp);
            tags.add(temp);
        }
        textView.setText(content);
        for (String tag : tags) {
            changeSpanTextColor(textView, tag, textView.getResources().getColor(R.color.comment_reply_more));
        }
    }
}
