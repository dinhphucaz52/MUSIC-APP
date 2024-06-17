package com.example.mymusicapp.OpenGL

import android.opengl.GLES32
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRenderer : GLSurfaceView.Renderer {

    companion object {
        init {
            System.loadLibrary("mymusicapp")
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//        GLES32.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        renderFrame()
    }

    private external fun renderFrame()

}