#include <jni.h>
#include <string>

/*声明要调用的方法*/
extern "C" {
extern int bsdiff_main(int argc, char *argv[]);
extern int bspatch_main(int argc, char *argv[]);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_crystallake_apkpatchlib_NativeLib_diff(JNIEnv *env, jclass clazz, jstring new_apk_path, jstring old_apk_path,
                                                jstring patch_path) {
    const char *newFile = env->GetStringUTFChars(new_apk_path, nullptr);
    const char *oldFile = env->GetStringUTFChars(old_apk_path, nullptr);
    const char *patchFile = env->GetStringUTFChars(patch_path, nullptr);

    char *argv[] = {"xeon_bs_diff", const_cast<char *>(oldFile), const_cast<char *>(newFile),
                    const_cast<char *>(patchFile)};
    /* 调用bsDiff的main方法，*/
    int res = bsdiff_main(4, argv);
    env->ReleaseStringUTFChars(old_apk_path, oldFile);
    env->ReleaseStringUTFChars(new_apk_path, newFile);
    env->ReleaseStringUTFChars(patch_path, patchFile);
    return res;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_crystallake_apkpatchlib_NativeLib_patch(JNIEnv *env, jclass clazz, jstring old_apk_path, jstring new_apk_path,
                                                 jstring patch_path) {
    const char *newFile = env->GetStringUTFChars(new_apk_path, nullptr);
    const char *oldFile = env->GetStringUTFChars(old_apk_path, nullptr);
    const char *patchFile = env->GetStringUTFChars(patch_path, nullptr);

    char *argv[] = {"xeon_bs_diff", const_cast<char *>(oldFile), const_cast<char *>(newFile),
                    const_cast<char *>(patchFile)};
    /* 调用bsDiff的main方法，*/
    int res = bsdiff_main(4, argv);
    env->ReleaseStringUTFChars(old_apk_path, oldFile);
    env->ReleaseStringUTFChars(new_apk_path, newFile);
    env->ReleaseStringUTFChars(patch_path, patchFile);
    return res;
}

