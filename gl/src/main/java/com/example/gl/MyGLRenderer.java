package com.example.gl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private static final String VERT = "attribute vec4 aPosition;\n" +
            "attribute vec2 aTexCoord;\n" +
            "varying vec2 vTexCoord;\n" +
            "void main(){\n" +
            "  vTexCoord = aTexCoord;\n" +
            "  gl_Position = aPosition;\n" +
            "}";

    private static final String FRAG = "precision mediump float;\n" +
            "varying vec2 vTexCoord;\n" +
            "uniform sampler2D uTex;\n" +
            "void main(){\n" +
            "  gl_FragColor = texture2D(uTex, vTexCoord);\n" +
            "}";

    private int program;
    private int texId;
    private int posLoc;
    private int texLoc;

    private final float[] vertices = {
            -1f, -1f, 0f, 0f,
             1f, -1f, 1f, 0f,
            -1f,  1f, 0f, 1f,
             1f,  1f, 1f, 1f,
    };
    private FloatBuffer vertexBuffer;

    private ByteBuffer pendingFrame; // RGBA
    private int frameWidth = 1;
    private int frameHeight = 1;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        program = buildProgram(VERT, FRAG);
        posLoc = GLES20.glGetAttribLocation(program, "aPosition");
        texLoc = GLES20.glGetAttribLocation(program, "aTexCoord");

        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        texId = tex[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);

        // Upload frame if pending
        ByteBuffer frame = pendingFrame;
        if (frame != null) {
            frame.position(0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
            GLES20.glTexImage2D(
                    GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_RGBA,
                    frameWidth,
                    frameHeight,
                    0,
                    GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,
                    frame
            );
        }

        int stride = 4 * 4;
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, stride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(posLoc);
        vertexBuffer.position(2);
        GLES20.glVertexAttribPointer(texLoc, 2, GLES20.GL_FLOAT, false, stride, vertexBuffer);
        GLES20.glEnableVertexAttribArray(texLoc);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public synchronized void updateFrame(ByteBuffer rgba, int width, int height) {
        this.pendingFrame = rgba;
        this.frameWidth = width;
        this.frameHeight = height;
    }

    private static int buildShader(int type, String src) {
        int s = GLES20.glCreateShader(type);
        GLES20.glShaderSource(s, src);
        GLES20.glCompileShader(s);
        return s;
    }

    private static int buildProgram(String vs, String fs) {
        int v = buildShader(GLES20.GL_VERTEX_SHADER, vs);
        int f = buildShader(GLES20.GL_FRAGMENT_SHADER, fs);
        int p = GLES20.glCreateProgram();
        GLES20.glAttachShader(p, v);
        GLES20.glAttachShader(p, f);
        GLES20.glLinkProgram(p);
        return p;
    }
}


