package com.example.calendarapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.calendarapp.Components.Interfaces.IComponent;
import com.example.calendarapp.Components.UserInfoComponent;
import com.example.calendarapp.R;

public class UserFragment extends Fragment implements IComponent {
    private View layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_user, container, false);

        initComponent(getContext());
        return layout;
    }
    @Override
    public void initComponent(Context context) {
        if(layout==null) return;
        UserInfoComponent userInfoComponent = layout.findViewById(R.id.userInfoComp);

        userInfoComponent.updateInfo();
    }
}
