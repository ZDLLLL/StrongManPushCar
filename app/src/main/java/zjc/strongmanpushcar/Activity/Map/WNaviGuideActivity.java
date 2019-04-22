package zjc.strongmanpushcar.Activity.Map;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWRouteGuidanceListener;
import com.baidu.mapapi.walknavi.adapter.IWTTSPlayer;
import com.baidu.mapapi.walknavi.model.RouteGuideKind;

public class WNaviGuideActivity extends Activity {
    WalkNavigateHelper mNaviHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化步行导航过程管理类
        mNaviHelper = WalkNavigateHelper.getInstance();
        // 在导航页面WNaviGuideActivity.this的onCreate方法中，调用mNaviHelper.onCreate(WNaviGuideActivity.this)创建诱导View。
        View view = mNaviHelper.onCreate(WNaviGuideActivity.this);
        if (view != null) {
            setContentView(view);
        }
//设置诱导信息回调监听，此组件只提供导航过程中的文本输出，不包含语音播报功能，需要自行传入对应的语音回调，形成播报功能。建议使用百度语音识别服务SDK。
//获取语音播报文本方法(注：该接口需要在startWalkNavi方法之前调用，否则不会有回调)：
        mNaviHelper.setTTsPlayer(new IWTTSPlayer() {
            /**
             * 诱导文本回调
             * @param s 诱导文本
             * @param b 是否抢先播报
             * @return
             */
            @Override
            public int playTTSText(String s, boolean b) {
                return 0;
            }
        });


        // 开始导航
        //导航状态信息，主要包括导航开始、结束，导航过程中偏航、偏航结束、诱导信息（包含诱导默认图标、诱导类型、诱导信息、剩余距离、时间、振动回调等
        mNaviHelper.startWalkNavi(WNaviGuideActivity.this);
        mNaviHelper.setRouteGuidanceListener(this, new IWRouteGuidanceListener() {
            @Override
            public void onRouteGuideIconUpdate(Drawable icon) {
                //诱导图标更新
            }

            @Override
            public void onRouteGuideKind(RouteGuideKind routeGuideKind) {
                //诱导枚举信息
            }

            @Override
            public void onRoadGuideTextUpdate(CharSequence charSequence, CharSequence charSequence1) {
                //诱导信息
            }

            @Override
            public void onRemainDistanceUpdate(CharSequence charSequence) {
                // 总的剩余距离
            }

            @Override
            public void onRemainTimeUpdate(CharSequence charSequence) {
                //总的剩余时间
            }

            @Override
            public void onGpsStatusChange(CharSequence charSequence, Drawable drawable) {
                //GPS状态发生变化，来自诱导引擎的消息
            }

            @Override
            public void onRouteFarAway(CharSequence charSequence, Drawable drawable) {
                //偏航信息
            }

            @Override
            public void onRoutePlanYawing(CharSequence charSequence, Drawable drawable) {
                //偏航规划中的信息
            }

            @Override
            public void onReRouteComplete() {
                //重新算路成功
            }

            @Override
            public void onArriveDest() {
                //到达目的地
            }

            @Override
            public void onIndoorEnd(Message message) {

            }

            @Override
            public void onFinalEnd(Message message) {

            }

            @Override
            public void onVibrate() {
                //震动
            }
        });
    }

    //在步行导航页面对应的acticity的生命周期方法中分别调用WalkNavigateHelper类中的对应生命周期方法。
    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNaviHelper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.quit();
    }
}



