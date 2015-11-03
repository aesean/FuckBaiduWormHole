package com.aesean.fuckbaiduwormhole;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.aesean.wromhole.CheckWormHole;

import java.util.List;

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

    private static void uninstall(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void check() {
        final List<String> defaultUrlList = CheckWormHole.getDefaultUrlList();
        tv_ResultDetail.setText("");
        CheckWormHole.check(defaultUrlList, new CheckWormHole.Callback() {
            boolean isSafe = true;

            int size = defaultUrlList.size();
            int temp = 0;
            StringBuilder stringBuilder = new StringBuilder();

            @Override
            public void wormHoleCheckResult(String url, int resultCode, String message) {
                temp++;
                if (CheckWormHole.isDangerous(resultCode)) {
                    isSafe = false;
                }

                stringBuilder.append(url).append(CheckWormHole.NEW_LINE)
                        .append(message).append(CheckWormHole.NEW_LINE);

                if (temp == size) {
                    tv_ResultDetail.setText(stringBuilder.toString());
                    if (isSafe) {
                        tv_Result.setText(getString(R.string.Safe));
                        tv_Result.setTextColor(getResources().getColor(R.color.Safe));
                    } else {
                        tv_Result.setText(R.string.Dangerous);
                        tv_Result.setTextColor(getResources().getColor(R.color.Dangerous));
                    }
                }
            }
        });
    }
}
