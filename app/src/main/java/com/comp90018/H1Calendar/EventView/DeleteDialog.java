package com.comp90018.H1Calendar.EventView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.comp90018.H1Calendar.R;


public class DeleteDialog extends Dialog implements View.OnClickListener{

    private TextView tvTitle,tvMessage,tvCancel,tvConfirm;

    private String title,message,cancel,confirm;

    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;

    public DeleteDialog(@NonNull Context context) {
        super(context);
    }
    public DeleteDialog(Context context, int themeId){
        super(context,themeId);
    }
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delete_dialog);
        tvTitle = findViewById(R.id.delete_dialog_title);
        tvMessage = findViewById(R.id.delete_dialog_message);
        tvCancel = findViewById(R.id.delete_dialog_cancel);
        tvConfirm = findViewById(R.id.delete_dialog_Confirm);

        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
        if(!TextUtils.isEmpty(message)){
            tvMessage.setText(message);
        }
        if(!TextUtils.isEmpty(confirm)){
            tvConfirm.setText(confirm);
        }
        if(!TextUtils.isEmpty(cancel)){
            tvCancel.setText(cancel);
        }

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCancel(String cancel,IOnCancelListener cancelListener) {
        this.cancel = cancel;
        this.cancelListener = cancelListener;
    }

    public void setConfirm(String confirm, IOnConfirmListener confirmListener) {
        this.confirm = confirm;
        this.confirmListener= confirmListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_dialog_cancel:
                if(cancelListener != null){
                    cancelListener.onCancel(this);
                }
                break;
            case R.id.delete_dialog_Confirm:
                if(confirmListener != null){
                    confirmListener.onConfirm(this);
                }
        }
    }

    public interface IOnCancelListener{
        void onCancel(DeleteDialog dialog);
    }
    public  interface IOnConfirmListener{
        void onConfirm(DeleteDialog dialog);
    }
}
