package com.example.arouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ARouter {
    private Map<String ,Class<? extends Activity>> activityList;

    public ARouter() {
        this.activityList = new HashMap<>();
    }

    private static class Holder{
        private static  ARouter aRouter = new ARouter();
    }
    public static ARouter getInstance() {
        return Holder.aRouter;
    }

    //activity对象存入List
    public void putActivity(String path,Class<? extends  Activity> clazz){
        if (path == null || clazz == null){
            return;
        }
        activityList.put(path,clazz);

    }
    private Context context;

    public void init(Application application) {
        this.context = application;
        try {
            Set<String> className = ClassUtils.getFileNameByPackageName(context,"com.example.utils");
            for (String name : className) {
                try {
                    Class<?> aClass = Class.forName(name);
                    //判断当前类是否是IRouter的实现类
                    if(IRoute.class.isAssignableFrom(aClass)){
                        IRoute iRoute= (IRoute) aClass.newInstance();
                        iRoute.putActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //跳转
    public void jupmActivity(String path, Bundle bundle) {
        Class<? extends Activity> aClass = activityList.get(path);
        if (aClass == null) {
            return;
        }
        Intent intent = new Intent().setClass(context, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        context.startActivity(intent);
    }
}
