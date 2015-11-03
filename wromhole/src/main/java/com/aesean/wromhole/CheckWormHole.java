package com.aesean.wromhole;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 检测WormHole工具类
 */
@SuppressWarnings("unused")
public class CheckWormHole {
    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final int CODE_SAFE = HttpURLConnection.HTTP_NOT_FOUND;
    public static final int CODE_DANGEROUS = HttpURLConnection.HTTP_OK;

    // 偷懒了，这里应该写进xml的。
    public static final String MESSAGE_SAFE = "安全";
    public static final String MESSAGE_DANGEROUS = "危险";

    /**
     * 线程池
     */
    private static ExecutorService mExec = Executors.newCachedThreadPool();

    private static CheckWormHole mCheckWormHole;

    private CheckWormHole() {

    }

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 检测是否有WormHole，网络请求必须异步，所以只能用回调方式完成请求。
     *
     * @param callback 回调接口，已经做了Handler通信，可以在接口中放心更新UI。
     */
    public static void check(Callback callback) {
        check(getDefaultUrlList(), callback);
    }

    /**
     * 检测是否有WormHole，网络请求必须异步，所以只能用回调方式完成请求。
     *
     * @param urlList  检测列表
     * @param callback 回调接口，已经做了Handler通信，可以在接口中放心更新UI。
     */
    public static void check(final List<String> urlList, final Callback callback) {
        for (String urlStr : urlList) {
            mExec.execute(getCheckRunnable(urlStr, callback));
        }
    }

    public static final String WORMHOLE_URL0 = "http://127.0.0.1:40310/daemon";
    public static final String WORMHOLE_URL1 = "http://127.0.0.1:6259/daemon";

    /**
     * 获取默认的WormHole Url List
     *
     * @return Url List
     */
    public static List<String> getDefaultUrlList() {
        List<String> list = new ArrayList<>();
        list.add(WORMHOLE_URL0);
        list.add(WORMHOLE_URL1);
        return list;
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        mExec.shutdown();
    }

    private static Runnable getCheckRunnable(final String urlStr, final Callback callback) {
        return new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL(urlStr);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    sendCallback(callback, urlStr, CODE_SAFE, MESSAGE_SAFE);
                    return;
                }
                HttpURLConnection conn;
                BufferedReader reader = null;
                InputStreamReader inputStreamReader = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();
                    StringBuilder stringBuilder = new StringBuilder();
                    inputStreamReader = new InputStreamReader(conn.getInputStream());
                    reader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append(NEW_LINE);
                    }
                    sendCallback(callback, urlStr, conn.getResponseCode(), stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    sendCallback(callback, urlStr, CODE_SAFE, MESSAGE_SAFE);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (inputStreamReader != null) {
                        try {
                            inputStreamReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    /**
     * 通过返回网页结果识别当前到底是什么应用有危险。
     *
     * @param message 网页返回结果
     * @return 危险应用列表
     */
    public static List<String> getAppName(String message) {
        String[] split = message.split(",");
        List<String> list = new ArrayList<>();
        Map<String, String> map = getKeywordsMap();
        for (String str : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (str.equals(entry.getKey())) {
                    list.add(entry.getValue());
                }
            }
        }
        return list;
    }

    /**
     * 返回结果与应用对应关系。我只测试了百度网盘，欢迎提交其他关键字。
     * 理论上这里可以通过包名称来定位APP的。偷懒了。
     *
     * @return 关键字Map
     */
    public static Map<String, String> getKeywordsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("com.baidu.netdisk", "百度网盘");
        return map;
    }

    private static void sendCallback(final Callback callback, final String url, final int code, final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.wormHoleCheckResult(url, code, message);
            }
        });
    }

    /**
     * 返回值是否有危险
     *
     * @param resultCode 返回值
     * @return true表示危险，false表示安全。
     */
    public static boolean isDangerous(int resultCode) {
        switch (resultCode) {
            case HttpURLConnection.HTTP_OK:
                return true;
            case HttpURLConnection.HTTP_NOT_FOUND:
                return false;
            default:
                return false;
        }
    }

    /**
     * 回调接口
     */
    public static interface Callback {
        /**
         * 检测结果的回调方法。
         *
         * @param url        检测的url
         * @param resultCode 返回值，理论上返回只要不是{@link HttpURLConnection#HTTP_NOT_FOUND}都可能存在危险，实际测试百度后门返回是{@link HttpURLConnection#HTTP_OK}
         * @param message    返回附加消息
         */
        void wormHoleCheckResult(String url, int resultCode, String message);
    }
}
