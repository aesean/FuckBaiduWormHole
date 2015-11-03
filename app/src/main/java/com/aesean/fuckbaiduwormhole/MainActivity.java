package com.aesean.fuckbaiduwormhole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aesean.wromhole.CheckWormHole;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_Result;
    private TextView tv_ResultDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.bt_Check).setOnClickListener(this);
        tv_Result = (TextView) findViewById(R.id.tv_Result);
        tv_ResultDetail = (TextView) findViewById(R.id.tv_ResultDetail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        check();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Check:
                check();
                break;
            default:
                break;
        }
    }

    private void check() {
        CheckWormHole.check(new CheckWormHole.Callback() {
            @Override
            public void wormHoleCheckResult(String url, int resultCode, String message) {
                if (CheckWormHole.isDangerous(resultCode)) {
                    tv_Result.setText(R.string.Dangerous);
                    tv_Result.setTextColor(getResources().getColor(R.color.Dangerous));
                    tv_ResultDetail.setText(tv_ResultDetail.getText().toString() + CheckWormHole.NEW_LINE + url);
                } else {
                    tv_Result.setText(R.string.Safe);
                    tv_Result.setTextColor(getResources().getColor(R.color.Safe));
                    tv_ResultDetail.setText("");
                }
            }
        });
    }
}
