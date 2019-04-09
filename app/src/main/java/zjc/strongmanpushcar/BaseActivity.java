package zjc.strongmanpushcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//基础设施活动，所有的活动都继承该活动
public class BaseActivity extends AppCompatActivity {
    protected Unbinder unbinder;
    ImmersionBar immersionBar ;//沉浸式状态栏
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);//每创建一个新活动就加入控制类中
        //去除标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //将状态栏设为透明
        immersionBar = ImmersionBar.with(this);
        immersionBar.init();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //去除标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @Override//重载setContentView函数
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //unbinder不能加在onCreate中,因为ButterKnife需要在setContentView之后才能Bind
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivityCollector.removeActivity(this);//每销毁一个新活动就从控制类中移除该活动
        ImmersionBar.with(this).destroy();
    }

}
