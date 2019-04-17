package zjc.strongmanpushcar.Activity.Entertainment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import butterknife.BindView;
import butterknife.OnClick;
import zjc.strongmanpushcar.Activity.Shopping.GuideActivity;
import zjc.strongmanpushcar.Adapter.VideoAdapter;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.Beans.DataUtil;
import zjc.strongmanpushcar.R;

public class MovieActivity extends BaseActivity {
    @BindView(R.id.movie_rv)
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        init();
    }
    private void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        VideoAdapter adapter = new VideoAdapter(this, DataUtil.getVideoListData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((VideoAdapter.VideoViewHolder) holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });
    }
    @OnClick(R.id.movie_back)
    public void movie_back_OnClick(){
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
    }
}
