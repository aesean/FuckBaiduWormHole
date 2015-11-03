package com.aesean.fuckbaiduwormhole;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aesean.wromhole.CheckWormHole;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_CheckResult1;
    private TextView tv_CheckResult2;
    private TextView tv_CheckResultApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.bt_Check).setOnClickListener(this);
        tv_CheckResult1 = (TextView) findViewById(R.id.tv_Result1);
        tv_CheckResult2 = (TextView) findViewById(R.id.tv_Result2);
        tv_CheckResultApp = (TextView) findViewById(R.id.tv_ResultApp);

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
                                    StringBuilder formatResult = new StringBuilder();
                                    formatResult.append(url).append(CheckWormHole.NEW_LINE)
                                            .append("返回值：").append(resultCode)
                                            .append("(").append(resultCode == CheckWormHole.CODE_SAFE ? CheckWormHole.MESSAGE_SAFE : CheckWormHole.MESSAGE_DANGEROUS).append(")").append(CheckWormHole.NEW_LINE)
                                            .append("返回消息：").append(message);
                                    if (url.equals(CheckWormHole.WORMHOLE_URL0)) {
                                        tv_CheckResult1.setText(formatResult.toString());
                                    } else {
                                        tv_CheckResult2.setText(formatResult.toString());
                                    }

                                    if (CheckWormHole.isDangerous(resultCode)) {
//                                        StringBuilder formatResultApp = new StringBuilder();
//                                        formatResultApp.append(tv_CheckResultApp.getText().toString()).append(CheckWormHole.NEW_LINE)
//                                                .append(message);
                                        tv_CheckResultApp.setTextColor(Color.RED);
                                        tv_CheckResultApp.setText(message);
                                    } else {
                                        tv_CheckResultApp.setTextColor(Color.GREEN);
                                        tv_CheckResultApp.setText(R.string.NotFound);
                                    }
                                }
                            }

        );
    }
}
