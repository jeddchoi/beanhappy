package edu.skku.se3.beanhappy;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rlaal on 2018-05-22.
 */

public class usingfragment extends Fragment{
    TextView textView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.usingfragment,null);


        textView = (TextView)view.findViewById(R.id.textView);
        return view;
                //super.onCreateView(inflater, container, savedInstanceState);
    }
}
