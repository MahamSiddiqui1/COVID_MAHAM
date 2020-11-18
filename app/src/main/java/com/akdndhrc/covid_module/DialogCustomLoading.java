package com.akdndhrc.covid_module;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.akdndhrc.covid_module.R;

/**
 * Created by Usman on 15/09/2015.
 */
public class DialogCustomLoading extends Dialog {

    Context context;
    Dialog dialog;

    public DialogCustomLoading(final Context context) {
        super(context);

        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lay_dialog_loading2);
        dialog.show();
   }


    public void DialogDismiss(){

        dialog.dismiss();
    }

    /*public void setText(String mTitle){
        TextView tv = (TextView) dialog.findViewById(R.id.tvTitle);
        tv.setText(mTitle);

    }*/

}



//AlertDialog.Builder builder =
//        new AlertDialog.Builder(ctx, R.style.AppCompatAlertDialogStyle);
//builder.setIcon(R.drawable.abc_btn_radio_to_on_mtrl_000);
//        builder.setTitle("Dialog");
//        builder.setMessage("Lorem ipsum dolor ....");
//        builder.setPositiveButton("OK", null);
//        builder.setNegativeButton("Cancel", null);
//        builder.show();


