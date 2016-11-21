package com.yf.girl.girl;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by lenovo on 2016/11/8.
 */

public class ImageDialog extends Dialog {
    private static int default_width=160;
    private static  int default_height=120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ImageDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


}
