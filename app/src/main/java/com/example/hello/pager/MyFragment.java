package com.example.hello.pager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;

public class MyFragment extends Fragment {
    private static BasePager basePager;

    public MyFragment(){

    }

    public static final MyFragment newInstance(BasePager pager) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        basePager = pager;
        fragment.setArguments(bundle);
        return fragment ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(basePager != null){
            return basePager.rootView;
        }
        return null;
    }
}
