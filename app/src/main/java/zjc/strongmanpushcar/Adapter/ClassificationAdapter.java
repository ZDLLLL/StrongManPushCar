package zjc.strongmanpushcar.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import zjc.strongmanpushcar.Activity.Shopping.GuideActivity;
import zjc.strongmanpushcar.Beans.StoreClassification;
import zjc.strongmanpushcar.R;

import static zjc.strongmanpushcar.MyApplication.getContext;

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationAdapter.ClassificationViewHodler>{
    public Context context;
    public LayoutInflater layoutInflater;
    public List<StoreClassification> list;
    public GuideActivity guideActivity;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    public  ClassificationAdapter( Context context,List<StoreClassification> list,GuideActivity guideActivity){
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.guideActivity = guideActivity;
        isClicks = new ArrayList<>();
        isClicks.add(true);
        for(int i = 1;i<list.size();i++){
            isClicks.add(false);
        }
    }


    @NonNull
    @Override
    public ClassificationViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  layoutInflater.inflate(R.layout.food_category_item,viewGroup,false);
        return new ClassificationViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificationViewHodler classificationViewHodler, final int i) {
        classificationViewHodler.catergory_text.setText(list.get(i).getClassificationName());
        //判断该项是否点击
        if (isClicks.get(i)){
            classificationViewHodler.catergory_ll.setBackgroundColor(Color.WHITE);
            guideActivity.getStoreData(i+1);
        }else{
            classificationViewHodler.catergory_ll.setBackgroundColor(Color.argb(180,241,241,241));
        }
        classificationViewHodler.catergory_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i <isClicks.size();i++){
                    isClicks.set(i,false);
                }
                isClicks.set(i,true);
                notifyDataSetChanged();//通知Adpter改变
            }
        });
//        Glide.with(getContext())
//                .load(list.get(i).getClassificationImg())
//                .asBitmap()
//                .error(R.drawable.love)
//                .into(classificationViewHodler.catergory_img);
           switch (i){
               case 0 :  classificationViewHodler.catergory_img.setImageResource(R.drawable.foods); break;
               case 1 : classificationViewHodler.catergory_img.setImageResource(R.drawable.buyfood);break;
               case 2 :  classificationViewHodler.catergory_img.setImageResource(R.drawable.techang);break;
               default: break;
           }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClassificationViewHodler extends RecyclerView.ViewHolder {
        LinearLayout catergory_ll;
        TextView catergory_text;
        ImageView catergory_img;
        public ClassificationViewHodler(@NonNull View itemView) {
            super(itemView);
            catergory_img = itemView.findViewById(R.id.catergory_img);
            catergory_text = itemView.findViewById(R.id.catergory_text);
            catergory_ll = itemView.findViewById(R.id.catergory_ll);
        }
    }
}
