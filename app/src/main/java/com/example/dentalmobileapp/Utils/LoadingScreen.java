package com.example.dentalmobileapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingScreen {

    private static ProgressDialog progressDialog;

    public static void showLoadingModal(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public static void hideLoadingModal() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
