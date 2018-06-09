package edu.skku.se3.beanhappy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import pl.polidea.view.ZoomView;

public class Showmap extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmap);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoom_item, null, false);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(10f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption("미니맵"); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        container.addView(zoomView);

        Button button = (Button) findViewById(R.id.map_OKBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}