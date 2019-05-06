package zjc.strongmanpushcar.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import zjc.strongmanpushcar.Beans.Food;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;

import static zjc.strongmanpushcar.MyApplication.getContext;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    public Context context;
    public LayoutInflater layoutInflater;
    public List<Food> list;
    public FoodAdapter(Context context,List<Food> list){
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.food_item,viewGroup,false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
        foodViewHolder.food_name.setText(list.get(i).getFoodName());
        foodViewHolder.food_price.setText(list.get(i).getFoodPrice());
        foodViewHolder.food_sale.setText(list.get(i).getFoodIntrouduce());
        Glide.with(getContext())
                .load(list.get(i).getFoodimg())
                .asBitmap()
                .error(R.drawable.image_1)
                .into(foodViewHolder.food_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView food_name;
        TextView food_sale;
        TextView food_price;
        ImageView food_img;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            food_img = itemView.findViewById(R.id.food_img);
            food_name = itemView.findViewById(R.id.food_name);
            food_sale = itemView.findViewById(R.id.food_sale);
            food_price = itemView.findViewById(R.id.food_price);
        }
    }
}
