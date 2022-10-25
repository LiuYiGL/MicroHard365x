package com.liuyi.weiying365;

import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedInit implements IXposedHookLoadPackage {

    private static final String TAG = "MicroHardHook";
    private static final int hookWidthPixels = 16;
    private static final int hookHeightPixels = 9;
    private static final List<String> pkgNameRegexList = Arrays.asList(
            "com.x1y9.probe",
            "com.microsoft.office.*");

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (pkgNameRegexList.stream().anyMatch(pkgNameRegex -> loadPackageParam.packageName.matches(pkgNameRegex))) {
            XposedBridge.log(TAG + "当前Hook的包名：" + loadPackageParam.packageName);
            hookRealMetrics(loadPackageParam);
            hookRealSize(loadPackageParam);
            hookDisplay(loadPackageParam);
            hookMetrics(loadPackageParam);
            hookGetWidth(loadPackageParam);
            hookGetHeight(loadPackageParam);
        }
    }


    /**
     * DisplayMetrics dm = new DisplayMetrics(); display.getRealMetrics(dm); height = dm.heightPixels;
     * 针对这种方式的获取分辨率
     */
    private static void hookRealMetrics(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod("android.view.Display",
                param.classLoader,
                "getRealMetrics",
                DisplayMetrics.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        try {
                            DisplayMetrics metrics = (DisplayMetrics) param.args[0];
                            metrics.widthPixels = hookWidthPixels;
                            metrics.heightPixels = hookHeightPixels;
                            Log.i(TAG, "hookRealMetrics");
                        } catch (Exception e) {
                            Log.e(TAG, "hookRealMetrics");
                        }
                    }

                });
    }

    /**
     * Rect rect = new Rect(); display.getRectSize(rect);;
     * 针对这种方式的获取分辨率
     */
    private static void hookRealSize(XC_LoadPackage.LoadPackageParam param) {

        XposedHelpers.findAndHookMethod("android.view.Display",
                param.classLoader,
                "getRealSize",
                Point.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        try {
                            Point point = (Point) param.args[0];
                            point.x = hookWidthPixels;
                            point.y = hookHeightPixels;
                            Log.i(TAG, "hookRealSize");
                        } catch (Exception e) {
                            Log.e(TAG, "hookRealSize");
                        }
                    }

                });
    }

    /**
     * Display display = wm.getDefaultDisplay();Point size = new Point();display.getSize(size);
     * 针对这种方式的获取分辨率
     */
    private static void hookDisplay(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod("android.view.Display",
                param.classLoader, "getSize", Point.class, new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        try {
                            Point mPoint = (Point) param.args[0];
                            mPoint.x = hookWidthPixels;
                            mPoint.y = hookHeightPixels;
                            Log.i(TAG, "hookDisplay");
                        } catch (Exception e) {
                            Log.e(TAG, "hookDisplay");
                        }
                    }
                });
    }

    /**
     * DisplayMetrics dm = new DisplayMetrics(); display.getMetrics(dm); height = dm.heightPixels;
     * 针对这种方式的获取分辨率
     */
    private static void hookMetrics(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod("android.view.Display",
                param.classLoader, "getMetrics", DisplayMetrics.class, new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        try {
                            DisplayMetrics metrics = (DisplayMetrics) param.args[0];
                            metrics.widthPixels = hookWidthPixels;
                            metrics.heightPixels = hookHeightPixels;
                            Log.i(TAG, "hookMetrics");
                        } catch (Exception e) {
                            Log.e(TAG, "hookMetrics");
                        }
                    }

                });
    }

    /**
     * Display.getWidth()  Display.getHeight()
     * 针对这种方式的获取分辨率
     */
    private static void hookGetWidth(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod("android.view.Display",
                param.classLoader, "getWidth", new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        try {
                            param.setResult(hookWidthPixels);
                            Log.i(TAG, "hookGetWidth");
                        } catch (Exception e) {
                            Log.e(TAG, "hookGetWidth");
                        }
                    }
                });
    }

    private static void hookGetHeight(XC_LoadPackage.LoadPackageParam param) {
        XposedHelpers.findAndHookMethod("android.view.Display", param.classLoader, "getHeight", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                try {
                    param.setResult(hookHeightPixels);
                    Log.i(TAG, "hookGetHeight");
                } catch (Exception e) {
                    Log.e(TAG, "hookGetHeight");
                }
            }
        });
    }

}
