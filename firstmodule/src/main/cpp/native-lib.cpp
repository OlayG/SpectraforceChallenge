#include <jni.h>
#include <string>
#include <android/log.h>
#include <GLES2/gl2.h>
#include <stdio.h>
#include <stdlib.h>

#define  LOG_TAG    "native-lib"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_firstmodule_JNILib_helloFromJNI(JNIEnv *env, jobject instance) {
    std::string hi = "Hi from C++";

    return env->NewStringUTF(hi.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_firstmodule_JNILib_toastRewind(JNIEnv *env, jobject instance) {
    std::string rewind = "Rewind";

    return env->NewStringUTF(rewind.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_firstmodule_JNILib_toastPlay(JNIEnv *env, jobject instance) {
    std::string play = "Play";

    return env->NewStringUTF(play.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_firstmodule_JNILib_toastFastForward(JNIEnv *env, jobject instance) {
    std::string fastForward = "Fast Forward";

    return env->NewStringUTF(fastForward.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_olayg_spectraforcechallenge_view_mainactivity_MainActivity_sendMessageToJni(
        JNIEnv *env, jobject instance, jstring message_) {
    const char *message = env->GetStringUTFChars(message_, 0);
    std::string fastForward = message;

    jclass mainActivity = env->GetObjectClass(instance);
    jmethodID callbackMainActivity = env->GetMethodID(mainActivity, "jniCallback", "(Ljava/lang/String;)V");

    if (NULL == callbackMainActivity) {
        //__android_log_print(ANDROID_LOG_ERROR, "TRACKERS", "%s", "Something Went Wrong");
        LOGI("Something Went Wrong");
        return;
    }

    env->CallVoidMethod(instance, callbackMainActivity, env->NewStringUTF(fastForward.c_str()));
    env->ReleaseStringUTFChars(message_, message);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_admin_shapegame_MyGLRenderer_drawTriangle(JNIEnv *env, jobject instance,
                                                           jobject triangle, jfloatArray scratch_) {
    jfloat *scratch = env->GetFloatArrayElements(scratch_, NULL);

    // TODO

    env->ReleaseFloatArrayElements(scratch_, scratch, 0);
}

static void printGLString(const char *name, GLenum s) {
    const char *v = (const char *) glGetString(s);
    LOGI("GL %s = %s\n", name, v);
}

static void checkGlError(const char* op) {
    for (GLint error = glGetError(); error; error
                                                    = glGetError()) {
        LOGI("after %s() glError (0x%x)\n", op, error);
    }
}

auto gVertexShader =
        "attribute vec4 vPosition;\n"
                "void main() {\n"
                "  gl_Position = vPosition;\n"
                "}\n";

auto gFragmentShader =
        "precision mediump float;\n"
                "void main() {\n"
                "  gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n"
                "}\n";

GLuint loadShader(GLenum shaderType, const char* pSource) {
    GLuint shader = glCreateShader(shaderType);
    if (shader) {
        glShaderSource(shader, 1, &pSource, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char* buf = (char*) malloc(infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    LOGE("Could not compile shader %d:\n%s\n",
                         shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
    }
    return shader;
}

GLuint createProgram(const char* pVertexSource, const char* pFragmentSource) {
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, pVertexSource);
    if (!vertexShader) {
        return 0;
    }

    GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, pFragmentSource);
    if (!pixelShader) {
        return 0;
    }

    GLuint program = glCreateProgram();
    if (program) {
        glAttachShader(program, vertexShader);
        checkGlError("glAttachShader");
        glAttachShader(program, pixelShader);
        checkGlError("glAttachShader");
        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char* buf = (char*) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}

GLuint gProgram;
GLuint gvPositionHandle;

bool setupGraphics(int w, int h) {
    printGLString("Version", GL_VERSION);
    printGLString("Vendor", GL_VENDOR);
    printGLString("Renderer", GL_RENDERER);
    printGLString("Extensions", GL_EXTENSIONS);

    LOGI("setupGraphics(%d, %d)", w, h);
    gProgram = createProgram(gVertexShader, gFragmentShader);
    if (!gProgram) {
        LOGE("Could not create program.");
        return false;
    }
    gvPositionHandle = glGetAttribLocation(gProgram, "vPosition");
    checkGlError("glGetAttribLocation");
    LOGI("glGetAttribLocation(\"vPosition\") = %d\n",
         gvPositionHandle);

    glViewport(0, 0, w, h);
    checkGlError("glViewport");
    return true;
}

const GLfloat gTriangleVertices[] = { 0.0f, 0.5f, -0.5f, -0.5f,
                                      0.5f, -0.5f };

void renderFrame() {
    static float grey;
    grey += 0.01f;
    if (grey > 1.0f) {
        grey = 0.0f;
    }
    glClearColor(grey, grey, grey, 1.0f);
    checkGlError("glClearColor");
    glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");

    glUseProgram(gProgram);
    checkGlError("glUseProgram");

    glVertexAttribPointer(gvPositionHandle, 2, GL_FLOAT, GL_FALSE, 0, gTriangleVertices);
    checkGlError("glVertexAttribPointer");
    glEnableVertexAttribArray(gvPositionHandle);
    checkGlError("glEnableVertexAttribArray");
    glDrawArrays(GL_TRIANGLES, 0, 3);
    checkGlError("glDrawArrays");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_firstmodule_JNILib_init(JNIEnv *env, jclass type, jint width, jint height) {

    setupGraphics(width, height);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_firstmodule_JNILib_step(JNIEnv *env, jclass type) {

    renderFrame();

}