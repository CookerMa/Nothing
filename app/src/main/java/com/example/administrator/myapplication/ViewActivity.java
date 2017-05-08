package com.example.administrator.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.Bezier.BubbleView;
import com.example.administrator.myapplication.Paint.LotteryCard;
import com.example.administrator.myapplication.Paint.YinYangView;

public class ViewActivity extends AppCompatActivity {

    private LinearLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        parent = (LinearLayout) findViewById(R.id.parent);

        layout();
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
            case 2:
                RotateArr ra = new RotateArr(ViewActivity.this);
                parent.addView(ra);
                break;
            case 3:
                BubbleView bv = new BubbleView(ViewActivity.this);
                parent.addView(bv);
                break;
            case 4:
                final LotteryCard lv = new LotteryCard(ViewActivity.this);
                parent.addView(lv);
                lv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lv.saveBitmap();
                    }
                });
                break;
            case 5:
                final YinYangView yy = new YinYangView(ViewActivity.this);
                parent.addView(yy);
                break;
        }
    }
}
