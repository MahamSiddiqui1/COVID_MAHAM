package com.akdndhrc.covid_module.slider;


import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.widget.Toast;

import com.akdndhrc.covid_module.Login_Activity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final Class<?> myActivityClass;

    public DefaultExceptionHandler(Context context, Class<?> c) {
        myContext = context;
        myActivityClass = c;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);// You can use LogCat too
       // Intent intent = new Intent(myContext, myActivityClass);
        Toast.makeText(myContext, "App is not working, please login again", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(myContext, Login_Activity.class);

        String s = stackTrace.toString();
        //you can use this String to know what caused the exception and in which Activity
        intent.putExtra("uncaughtException", "Exception is: " + stackTrace.toString());
        intent.putExtra("stacktrace", s);
        myContext.startActivity(intent);

        //for restarting the Activity
        Process.killProcess(Process.myPid());
        System.exit(0);

        //startActivity(new Intent(Register.this, Floatingcircular.class));
    }
}
