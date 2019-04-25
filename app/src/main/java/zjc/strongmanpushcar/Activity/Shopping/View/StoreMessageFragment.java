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
import android.widget.ImageView;

import zjc.strongmanpushcar.Activity.Shopping.ShoppingActivity;
import zjc.strongmanpushcar.R;

public class StoreMessageFragment extends DialogFragment {
    Button store_mes_bt;
    ImageView store_mes_back;
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
        store_mes_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        store_mes_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoppingActivity.class);
                getActivity().startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}
