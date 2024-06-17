#include <vector>
#include <jni.h>
#include <GLES3/gl3.h>

struct rgb {
    rgb(int red, int green, int blue) {
        this->red = red;
        this->green = green;
        this->blue = blue;
    }
    int red;
    int green;
    int blue;
};

std::vector<rgb> rbg_list = {
        rgb(33, 37, 43),
        rgb(1, 93, 70),
        rgb(85, 244, 146),
        rgb(0, 130, 252),
        rgb(229, 115, 115),
        rgb(255, 203, 107),
        rgb(242, 107, 0),
        rgb(38, 162, 193),
        rgb(94, 106, 240)
};

int dem = 0;
int cnt = 0;

void glClearColor(rgb rgb1, float d);

extern "C"
JNIEXPORT void JNICALL
Java_com_example_mymusicapp_OpenGL_MyRenderer_renderFrame(JNIEnv *env, jobject thiz) {
    dem++;
    if (dem % 120 == 0){
        cnt = (cnt + 1) % (int)rbg_list.size();
        if (dem / 120 == 100)
            dem %= 120;
    }
    glClearColor(rbg_list[cnt], 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
}

void glClearColor(rgb rgb, float d) {
    GLfloat red = (float) rgb.red / 255;
    GLfloat green = (float) rgb.green / 255;
    GLfloat blue = (float) rgb.blue / 255;
    glClearColor(red, green, blue, 1.0f);
}