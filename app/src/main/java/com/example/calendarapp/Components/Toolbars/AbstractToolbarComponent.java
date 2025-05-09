package com.example.calendarapp.Components.Toolbars;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.calendarapp.Components.Interfaces.IComponent;

public abstract class AbstractToolbarComponent extends LinearLayout implements IComponent {

    protected FrameLayout flFragmentContainer;
    protected FragmentManager fragmentManager;
    protected Class<? extends Fragment> currentFragmentClass;
    protected int selectedBtnId;

    public AbstractToolbarComponent(Context context) {
        super(context);
    }

    public AbstractToolbarComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractToolbarComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbstractToolbarComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected final OnClickListener changeFragment = v -> {
        if (fragmentManager == null || flFragmentContainer == null) return;
        Class<? extends Fragment> clazz = getFragmentClassForButtonId(v.getId());
        if (clazz != null) {
            try {
                selectedBtnId = v.getId();
                currentFragmentClass = clazz;
                setButtonGreyer((ImageButton) v);
                replaceFragment();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    protected void replaceFragment() {
        try {
            if (currentFragmentClass == null) currentFragmentClass = getDefaultFragmentClass();
            Fragment fragment = instantiateFragment(currentFragmentClass);
            fragmentManager.beginTransaction()
                    .replace(flFragmentContainer.getId(), fragment)
                    .commit();
            if (fragment instanceof IComponent) {
                ((IComponent) fragment).initComponent(getContext());
            }
        } catch (Exception e) {
            Log.e("AbstractToolbar", "Error replacing fragment", e);
        }
    }

    protected void setButtonGreyer(ImageButton btn) {
        btn.setEnabled(false);
        btn.setColorFilter(Color.parseColor("#919191"));
        resetAllButtonsBut(btn);
    }

    protected abstract void resetAllButtonsBut(ImageButton activeButton);

    protected abstract Class<? extends Fragment> getFragmentClassForButtonId(int id);

    protected abstract Class<? extends Fragment> getDefaultFragmentClass();

    protected Fragment instantiateFragment(Class<? extends Fragment> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }

    public void initFragmentManagement(FrameLayout flFragmentContainer, FragmentManager fragmentManager) {
        if (this.fragmentManager == null && this.flFragmentContainer == null) {
            this.fragmentManager = fragmentManager;
            this.flFragmentContainer = flFragmentContainer;
        }
        replaceFragment();
    }
}
