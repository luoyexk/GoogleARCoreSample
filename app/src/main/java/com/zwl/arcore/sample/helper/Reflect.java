package com.zwl.arcore.sample.helper;

import android.os.Handler;

import com.google.ar.sceneform.ux.TwistGesture;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflect {

    public static void change() throws NoSuchFieldException, IllegalAccessException {
        //获取Bean类的INT_VALUE字段
        Field field = TwistGesture.class.getField("TWIST_GESTURE_DEBUG");
        //将字段的访问权限设为true：即去除private修饰符的影响
        field.setAccessible(true);
        /*去除final修饰符的影响，将字段设为可修改的*/
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        //把字段值设为200
        field.set(null, true);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.post(this);
            }
        });

    }
}
