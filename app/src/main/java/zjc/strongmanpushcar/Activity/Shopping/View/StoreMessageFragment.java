package zjc.strongmanpushcar.Activity.Shopping.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import zjc.strongmanpushcar.Activity.Map.GaoDeNavActivity;
import zjc.strongmanpushcar.Activity.Shopping.ShoppingActivity;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;

import static zjc.strongmanpushcar.MyApplication.getContext;

public class StoreMessageFragment extends DialogFragment {
    Button store_mes_bt;
    ImageView store_mes_back;
    TextView averconsumption_tv;
    TextView store_name_tv;
    TextView store_location_tv;
    ImageView store_mes_img;
    ImageView nav_bt;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.store_message);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置fragment的背景为透明
        initView(dialog);
        // 设置宽度为屏宽, 靠近屏幕中央。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);//动画
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // 中部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 1/2;
        window.setAttributes(lp);
        return dialog;
    }
    public void initView(final Dialog dialog){
        store_mes_bt = dialog.findViewById(R.id.store_mes_bt);
        store_mes_back = dialog.findViewById(R.id.store_mes_back);
        store_location_tv=dialog.findViewById(R.id.store_location_tv);
        store_name_tv=dialog.findViewById(R.id.store_name_tv);
        nav_bt=dialog.findViewById(R.id.nav_bt);
        averconsumption_tv=dialog.findViewById(R.id.averconsumption_tv);
        store_mes_img=dialog.findViewById(R.id.store_mes_img);
        store_mes_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        store_name_tv.setText(MyApplication.getShopname());
        averconsumption_tv.setText(MyApplication.getShopprice());

        Glide.with(getContext())
                .load(MyApplication.getShopimage())
                .asBitmap()
                .error(R.drawable.kfc)
                .into(store_mes_img);
        store_mes_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoppingActivity.class);
                intent.putExtra("shopId",MyApplication.getShopId());
                getActivity().startActivity(intent);
                dialog.dismiss();
            }
        });
        nav_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), GaoDeNavActivity.class);
                intent.putExtra("endLongitude",MyApplication.getStoreLongitude());
                intent.putExtra("endLatitude",MyApplication.getStoreLatitude());
                intent.putExtra("startLatitude",MyApplication.getmCurrentLantitude());
                intent.putExtra("startLongitude",MyApplication.getmCurrentLongitude());

                getActivity().startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}
