package zjc.strongmanpushcar.Servers.ServerImp;

import android.os.Looper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import zjc.strongmanpushcar.Activity.Message.MessageActivity;
import zjc.strongmanpushcar.Beans.Message;
import zjc.strongmanpushcar.Internet.Net;
import zjc.strongmanpushcar.Internet.OKHttp;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.Servers.Server.MessageServer;

public class MessageServerImp implements MessageServer {
    MessageActivity messageActivity;
    public MessageServerImp(MessageActivity messageActivity){
        this.messageActivity = messageActivity;
    }
    @Override
    public void findAllFlight() {
        String URL = Net.findAllFlight;
        OKHttp.sendOkhttpGetRequest(URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MyApplication.getContext(),"得到机票信息失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ResponseData=response.body().string();
                try {
                    List<Message> list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(ResponseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0 ; i < jsonArray.length();i++){
                         JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                         Message message = new Message();
                         message.setArrival(jsonObject1.getString("arrival"));
                         message.setArrivalTime(jsonObject1.getString("arrivalTime"));
                         message.setCheckPort(jsonObject1.getString("checkPort"));
                         message.setDeparture(jsonObject1.getString("departure"));
                         message.setDepartureTime(jsonObject1.getString("departureTime"));
                         message.setFlightId(jsonObject1.getString("flightId"));
                         list.add(message);
                    }
                    messageActivity.CallBack(list);
                }catch (Exception e){
                    Looper.prepare();
                    Toast.makeText(MyApplication.getContext(),"哪里出错了",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }
}
