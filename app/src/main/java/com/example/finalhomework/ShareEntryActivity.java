//package com.example.finalhomework;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.alipay.share.sdk.openapi.IAPApi;
//import com.alipay.share.sdk.openapi.IAPAPIEventHandler;
//
//public class ShareEntryActivity extends Activity {
//    private static final String TAG = "ShareEntryActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // 初始化支付宝 API
//        IAPApi api = new IAPApi();
//
//        // 获取支付宝返回的 Intent，并处理
//        Intent intent = getIntent();
//        api.handleIntent(intent, new IAPAPIEventHandler() {
//            @Override
//            public void onResp(com.alipay.share.sdk.openapi.BaseResp resp) {
//                if (resp.errCode == com.alipay.share.sdk.openapi.BaseResp.ErrCode.ERR_OK) {
//                    // 支付成功
//                    Log.d(TAG, "支付成功");
//                } else {
//                    // 支付失败
//                    Log.d(TAG, "支付失败");
//                }
//            }
//        });
//
//        // 完成处理后返回到主界面
//        finish();
//    }
//}
