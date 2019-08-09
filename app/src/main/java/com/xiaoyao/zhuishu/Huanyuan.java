package com.xiaoyao.zhuishu;

import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Huanyuan implements IXposedHookLoadPackage {

    private ClassLoader mClassLoader;
    private Context mContext;

    @Override
    public void handleLoadPackage (XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.wumii.android.athena")) {

            XC_MethodHook.Unhook andHookMethod = XposedHelpers.findAndHookMethod("com.stub.StubApp", loadPackageParam.classLoader,
                    "attachBaseContext", Context.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod (MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            //获取到Context对象，通过这个对象来获取classloader
                            Context context = (Context) param.args[0];
                            //获取classloader，之后hook加固后的就使用这个classloader
                            ClassLoader classLoader = context.getClassLoader();
                            //下面就是强classloader修改成壳的classloader就可以成功的hook了
                            XposedHelpers.findAndHookMethod("com.wumii.android.athena.model.realm.Config", classLoader, "getVip", new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod (MethodHookParam param) throws Throwable {
                                    super.beforeHookedMethod(param);

                                }

                                @Override
                                protected void afterHookedMethod (MethodHookParam param) throws Throwable {
                                    super.afterHookedMethod(param);
                                    XposedBridge.log("hook前" +  param.getResult());
                                    param.setResult(true);
                                    XposedBridge.log("hook后" +  param.getResult());
                                }
                            });

                        }

                    });

        }

    }
}