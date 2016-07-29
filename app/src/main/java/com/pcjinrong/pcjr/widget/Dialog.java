package com.pcjinrong.pcjr.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Mario on 2016/6/20.
 */
public class Dialog {
    public static void show(String title, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    public static void show(String title,String msg, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

}