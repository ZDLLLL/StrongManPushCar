package zjc.strongmanpushcar.Activity.Entertainment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.BaseTools.HomeKeyWatcher;

public class CompatHomeKeyActivity extends BaseActivity {
    private boolean pressedHome;
    private HomeKeyWatcher mHomeKeyWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHomeKeyWatcher = new HomeKeyWatcher(this);
        mHomeKeyWatcher.setOnHomePressedListener(new HomeKeyWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                pressedHome = true;
            }
        });
        pressedHome = false;
        mHomeKeyWatcher.startWatch();
    }

    @Override
    protected void onStop() {
        // 在OnStop中是release还是suspend播放器，需要看是不是因为按了Home键
        if (pressedHome) {
            NiceVideoPlayerManager.instance().suspendNiceVideoPlayer();
        } else {
            NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
        }
        super.onStop();
        mHomeKeyWatcher.stopWatch();
    }

    @Override
    protected void onRestart() {
        mHomeKeyWatcher.startWatch();
        pressedHome = false;
        super.onRestart();
        NiceVideoPlayerManager.instance().resumeNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return;
        }
        super.onBackPressed();
    }

}
