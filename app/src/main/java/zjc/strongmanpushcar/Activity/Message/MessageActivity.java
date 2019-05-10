package zjc.strongmanpushcar.Activity.Message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import zjc.strongmanpushcar.Adapter.MessageAdapter;
import zjc.strongmanpushcar.BaseTools.BaseActivity;
import zjc.strongmanpushcar.Beans.Message;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;
import zjc.strongmanpushcar.Servers.Server.MessageServer;
import zjc.strongmanpushcar.Servers.ServerImp.MessageServerImp;

public class MessageActivity extends BaseActivity {
    @BindView(R.id.message_people_id)
    TextView message_people_id;
    @BindView(R.id.message_rv)
    RecyclerView message_rv;
    MessageAdapter messageAdapter;
    MessageServer messageServer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message_people_id.setText(MyApplication.getIdCard());
        messageServer = new MessageServerImp(this);
        messageServer.findAllFlight();
        message_rv.setNestedScrollingEnabled(false);
        message_rv.setLayoutManager(new LinearLayoutManager(this));

    }
    public void CallBack(final List<Message> list){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter = new MessageAdapter(MessageActivity.this,list);
                message_rv.setAdapter(messageAdapter);
            }
        });
    }

}
