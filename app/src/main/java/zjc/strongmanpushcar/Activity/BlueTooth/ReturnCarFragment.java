package zjc.strongmanpushcar.Activity.BlueTooth;

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
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import zjc.strongmanpushcar.Activity.Shopping.ShoppingActivity;
import zjc.strongmanpushcar.BaseTools.ClientManager;
import zjc.strongmanpushcar.MyApplication;
import zjc.strongmanpushcar.R;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

public class ReturnCarFragment extends DialogFragment {
    TextView return_car_no;
    TextView return_car_yes;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.return_car_fragment);
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
        return_car_no  = dialog.findViewById(R.id.return_car_no);
        return_car_yes = dialog.findViewById(R.id.return_car_yes);
        return_car_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientManager.getClient().write(MyApplication.getMac(),MyApplication.getServer(),MyApplication.getCharacteristic(), ByteUtils.stringToBytes("62"), mWriteRsp);
                dialog.dismiss();
            }
        });
        return_car_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientManager.getClient().write(MyApplication.getMac(),MyApplication.getServer(),MyApplication.getCharacteristic(), ByteUtils.stringToBytes("61"), mWriteRsp);
                dialog.dismiss();
            }
        });
    }
    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                Toast.makeText(MyApplication.getContext(),"发送成功",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MyApplication.getContext(),"发送失败",Toast.LENGTH_LONG).show();
            }
        }
    };
}
