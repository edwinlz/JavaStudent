package com.usac.clasesjava;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usac.javastudent.R;

/**
 * Created by Edwin on 30/05/2016.
 */
public class TabAmigo extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }
}