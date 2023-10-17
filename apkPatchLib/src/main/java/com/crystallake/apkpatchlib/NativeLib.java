package com.crystallake.apkpatchlib;

public class NativeLib {

    // Used to load the 'apkpatchlib' library on application startup.
    static {
        System.loadLibrary("apkpatchlib");
    }

    public static native int patch(String oldApkPath, String newApkPath,
                                   String patchPath);

    public static native int diff(String newApkPath,String oldApkPath,String patchPath);

}