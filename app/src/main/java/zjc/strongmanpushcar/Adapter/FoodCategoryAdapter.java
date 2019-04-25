package zjc.strongmanpushcar.Adapter;

import android.app.LauncherActivity;
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

import java.util.ArrayList;
import java.util.List;

import zjc.strongmanpushcar.Activity.Shopping.ShoppingActivity;
import zjc.strongmanpushcar.Beans.FoodCatergory;
import zjc.strongmanpushcar.R;

/*
 * 该Adapter为商店食物分类栏的Adapter
 */
public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.CategoryViewHolder> {
    public Context context;
    public LayoutInflater layoutInflater;
    public List<FoodCatergory> list;
    public ShoppingActivity shoppingActivity;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色
    public FoodCategoryAdapter(Context context, List<FoodCatergory> list, ShoppingActivity shoppingActivity){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.shoppingActivity = shoppingActivity;
        isClicks = new ArrayList<>();
        isClicks.add(true);
        for(int i = 1;i<list.size();i++){
            isClicks.add(false);
        }
    }
    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_category_item,viewGroup,false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, final int i) {
       categoryViewHolder.catergory_img.setImageResource(R.drawable.image_1);
       categoryViewHolder.catergory_text.setText(list.get(i).getCatergryName());
       //判断该项是否点击
       if (isClicks.get(i)){
           categoryViewHolder.catergory_ll.setBackgroundColor(Color.WHITE);
           shoppingActivity.getFood(list.get(i).getCatergryId());
       }else{
           categoryViewHolder.catergory_ll.setBackgroundColor(Color.argb(180,241,241,241));
       }
       categoryViewHolder.catergory_ll.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               for(int i = 0; i <isClicks.size();i++){
                   isClicks.set(i,false);
               }
               isClicks.set(i,true);
               notifyDataSetChanged();//通知Adpter改变
           }
       });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout catergory_ll;
        TextView catergory_text;
        ImageView catergory_img;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            catergory_img = itemView.findViewById(R.id.catergory_img);
            catergory_text = itemView.findViewById(R.id.catergory_text);
            catergory_ll = itemView.findViewById(R.id.catergory_ll);

        }

    }




}
