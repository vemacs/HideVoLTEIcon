package es.nkmem.da.voltehider;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedHook implements IXposedHookLoadPackage {
    public static String TAG = "[Hide VoLTE Icon] ";
    public static final String PACKAGE_SYSTEMUI = "com.android.systemui";
    private static final String CLASS_SIGNAL_CLUSTER_VIEW = "com.android.systemui.statusbar.SignalClusterView";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(PACKAGE_SYSTEMUI)) {
            return;
        }
        XposedBridge.log(TAG + "Hooking SignalClusterView");
        Class<?> classSignalClusterView = XposedHelpers.findClass(CLASS_SIGNAL_CLUSTER_VIEW, lpparam.classLoader);
        XposedHelpers.findAndHookMethod(classSignalClusterView, "apply", onApplyHook);
    }

    private static XC_MethodHook onApplyHook = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            String volteVisible = "mVolteVisible";
            if (XposedHelpers.getBooleanField(param.thisObject, volteVisible)) {
                XposedBridge.log(TAG + "Hiding VoLTE view");
                XposedHelpers.setBooleanField(param.thisObject, volteVisible, false);
            }
        }
    };
}
