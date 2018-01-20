package com.example.firstmodule;

/**
 * Created by olayg on 1/19/2018.
 */

public class JNILib {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String helloFromJNI();

    /**
     * Provides String for toast.
     */
    public native String toastRewind();

    /**
     * Provides String for toast.
     */
    public native String toastPlay();

    /**
     * Provides String for toast.
     */
    public native String toastFastForward();

    /**
     * @param width the current view width
     * @param height the current view height
     */
    public static native void init(int width, int height);
    public static native void step();

}
