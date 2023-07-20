#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_internet_speedtest_speedcheck_nvboost_utils_Constant_getMainApi(
        JNIEnv *env, jclass clazz) {

    std::string hello = "https://goldenapps.space/nv/WifiSpeed462/V1/";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL

Java_com_internet_speedtest_speedcheck_nvboost_utils_Constant_getKey1(
        JNIEnv *env, jclass clazz) {

    std::string hello = "wAObio362ez9xWEu";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_internet_speedtest_speedcheck_nvboost_utils_Constant_getKey2(
        JNIEnv *env, jclass clazz) {

    std::string hello = "Ooi09Xd2tM59csKM";
    return env->NewStringUTF(hello.c_str());
}

