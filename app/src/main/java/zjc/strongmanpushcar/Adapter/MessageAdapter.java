package zjc.strongmanpushcar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zjc.strongmanpushcar.Beans.Message;
import zjc.strongmanpushcar.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHodler> {
    Context context;
    LayoutInflater layoutInflater;
    List<Message> list;
    public MessageAdapter(Context context, List<Message> list){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list =list;
    }
    @NonNull
    @Override
    public MessageViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.message_item,viewGroup,false);

        return new MessageViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHodler messageViewHodler, int i) {
         messageViewHodler.message_id.setText(list.get(i).getFlightId());
        messageViewHodler.message_departure.setText(list.get(i).getDeparture());
        messageViewHodler.message_arrival.setText(list.get(i).getArrival());
        messageViewHodler.message_departureTime.setText(list.get(i).getDepartureTime());
        messageViewHodler.message_arrivalTime.setText(list.get(i).getArrivalTime());
        messageViewHodler.message_checkPort.setText(list.get(i).getCheckPort());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHodler extends RecyclerView.ViewHolder {
        TextView message_id;
        TextView message_departure;
        TextView message_arrival;
        TextView message_departureTime;
        TextView message_arrivalTime;
        TextView message_checkPort;
        public MessageViewHodler(@NonNull View itemView) {
            super(itemView);
            message_id = itemView.findViewById(R.id.message_id);
            message_departure = itemView.findViewById(R.id.message_departure);
            message_arrival = itemView.findViewById(R.id.message_arrival);
            message_departureTime = itemView.findViewById(R.id.message_departureTime);
            message_arrivalTime = itemView.findViewById(R.id.message_arrivalTime);
            message_checkPort = itemView.findViewById(R.id.message_checkPort);

        }
    }
}
