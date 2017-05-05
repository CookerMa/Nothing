package com.example.administrator.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ViewActivity extends AppCompatActivity {

    private LinearLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        parent = (LinearLayout) findViewById(R.id.parent);

        final MagicView mgv = (MagicView) findViewById(R.id.mg);
        mgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgv.startAnimation();
            }
        });

//        layout();
    }

    private void layout() {
        parent.removeAllViews();
        switch (getIntent().getIntExtra("pos",0))
        {
            default:
            case 0:
                break;
            case 1:
                MagicView mgv = new MagicView(ViewActivity.this);
                parent.addView(mgv);
                mgv.startAnimation();
                break;
        }
    }
}
