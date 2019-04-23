package zjc.strongmanpushcar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;


import zjc.strongmanpushcar.Activity.Map.util.RecyclerUtils;
import zjc.strongmanpushcar.Activity.Map.util.FlightRecyclerItemView;
import zjc.strongmanpushcar.R;


public class FlightRecyclerViewAdapter extends RecyclerView.Adapter<FlightRecyclerViewAdapter.SimpleHolder>
        implements FlightRecyclerItemView.onSlidingButtonListener{
    private Context context;
    public String e_phone;
    private String coc_id;
    private List<String> dataName;     //姓名
    private List<String> dataid;

    private onSlidingViewClickListener onSvcl;

    private FlightRecyclerItemView recyclers;
    public FlightRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public FlightRecyclerViewAdapter(Context context,
                                     List<String> dataName, List<String> dataid) {
        this.context = context;
        this.dataName = dataName;
        this.dataid=dataid;
    }

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.flight_item, parent, false);
        return new SimpleHolder((FlightRecyclerItemView) view);
    }

    @Override
    public void onBindViewHolder(final SimpleHolder holder, final int position) {
        //holder.check_image_iv.setImageBitmap(dataImage.get(position));
//
//        holder.goodsclass_name_tv.setText(dataName.get(position));
//        holder.goodsclass_id_tv.setText(dataid.get(position));

        holder.flight_slide.getLayoutParams().width = RecyclerUtils.getScreenWidth(context);

        holder.flight_slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(context,"做出操作，进入新的界面或弹框",Toast.LENGTH_SHORT).show();
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    //获得布局下标（点的哪一个）
                    int subscript = holder.getLayoutPosition();
                    onSvcl.onItemClick(view, subscript);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onMenuIsOpen(View view) {
        recyclers = (FlightRecyclerItemView) view;
    }

    @Override
    public void onDownOrMove(FlightRecyclerItemView recycler) {
        if(menuIsOpen()){
            if(recyclers != recycler){
                closeMenu();
            }
        }
    }





    class SimpleHolder extends  RecyclerView.ViewHolder {
        public ImageButton left_ib;
//        public TextView goodsclass_name_tv;
//        public TextView goodsclass_id_tv;
//        public TextView delete_goodsclass_tv;
        public LinearLayout flight_slide;
        public SimpleHolder(FlightRecyclerItemView view) {
            super(view);
            left_ib = view.findViewById(R.id.left_ib);
//            delete_goodsclass_tv = (TextView) view.findViewById(R.id.delete_goodsclass_tv);
            flight_slide = (LinearLayout) view.findViewById(R.id.flight_slide);
//            goodsclass_id_tv = (TextView) view.findViewById(R.id.goodsclass_id_tv);

            view.setSlidingButtonListener(FlightRecyclerViewAdapter.this);
        }
    }

    //删除数据
    public void removeData(int position){
        dataName.remove(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position);

    }

    //关闭菜单
    public void closeMenu() {
        recyclers.closeMenu();
        recyclers = null;

    }

    // 判断是否有菜单打开
    public Boolean menuIsOpen() {
        if(recyclers != null){
            return true;
        }
        return false;
    }

    //设置在滑动侦听器上
    public void setOnSlidListener(onSlidingViewClickListener listener) {
        onSvcl = listener;
    }

    // 在滑动视图上单击侦听器
    public interface onSlidingViewClickListener {
        void onItemClick(View view, int position);
        void onDeleteBtnCilck(View view, int position);
    }


}
