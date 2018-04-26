package com.xsl.secondhand.widget.navigationbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2017/11/28,Time:11:45
 * Description:利用反射设置>4的问题
 */

public class BottomNavigationBar extends BottomNavigationView {
    public BottomNavigationBar(Context context) {
        super(context);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("RestrictedApi")
    public void setNormalBar() {

        BottomNavigationMenuView mMenuView = getField(getClass().getSuperclass(), this, "mMenuView", BottomNavigationMenuView.class);
        BottomNavigationItemView[] mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons", BottomNavigationItemView[].class);
        for (BottomNavigationItemView button : mButtons) {
            TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel", TextView.class);
            TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel", TextView.class);

            setField(button.getClass(), button, "mShiftAmount", 0);
            setField(button.getClass(), button, "mScaleUpFactor", 1);
            setField(button.getClass(), button, "mScaleDownFactor", 1);

            mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabel.getTextSize());
            mSmallLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabel.getTextSize());
            setField(button.getClass(), button, "mShiftingMode", false);
        }
        setField(mMenuView.getClass(), mMenuView, "mShiftingMode", false);

        mMenuView.updateMenuView();
    }


    public int getCurrentItem() {

        BottomNavigationMenuView mMenuView = getField(getClass().getSuperclass(), this, "mMenuView", BottomNavigationMenuView.class);
        BottomNavigationItemView[] mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons", BottomNavigationItemView[].class);
        Menu menu = getMenu();
        for (int i = 0; i < mButtons.length; i++) {
            if (menu.getItem(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    public void setCurrentItem(int item) {

        if (item < 0 || item >= getMaxItemCount()) {
            throw new ArrayIndexOutOfBoundsException("item is out of bounds, we expected 0 - "
                    + (getMaxItemCount() - 1) + ". Actually " + item);
        }

        BottomNavigationMenuView mMenuView = getField(getClass().getSuperclass(), this, "mMenuView", BottomNavigationMenuView.class);
        BottomNavigationItemView[] mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons", BottomNavigationItemView[].class);
        OnClickListener mOnClickListener = getField(mMenuView.getClass(), mMenuView, "mOnClickListener", OnClickListener.class);
        mOnClickListener.onClick(mButtons[item]);
    }

    /**
     * get private filed in this specific object
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param fieldType   the field type
     * @param <T>
     * @return field if success, null otherwise.
     */
    private <T> T getField(Class targetClass, Object instance, String fieldName, Class<T> fieldType) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * change the field value
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param value
     */
    private void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

