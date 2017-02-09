package com.baishan.viewdragdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class MainActivity extends AppCompatActivity {

    private LeftDrawerLayout mLeftDrawerLayout;
    private TextView mContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
//        mContentTv = (TextView) findViewById(R.id.id_content_tv);
//        mContentTv.setOnClickListener(v->mLeftDrawerLayout.openDrawer());

        // Create a system to run the physics loop for a set of springs.
        SpringSystem springSystem = SpringSystem.create();

        // Add a spring to the system.
        Spring spring = springSystem.createSpring();
        SpringConfig springConfig = SpringConfig.fromBouncinessAndSpeed(15, 10);
        spring.setSpringConfig(springConfig);
        // Add a listener to observe the motion of the spring.
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                System.out.println("value---"+value);
                float scale = 1f - (value * 0.5f);
//                myView.setScaleX(scale);
//                myView.setScaleY(scale);
            }

        });

        // Set the spring in motion; moving from 0 to 1
        spring.setEndValue(1);
    }
}
