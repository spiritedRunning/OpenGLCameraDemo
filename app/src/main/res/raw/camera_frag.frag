#extension GL_OES_EGL_image_external : require   // 使用摄像头时需要开启

precision mediump float;  // 数据精度
varying vec2 aCoord;

uniform samplerExternalOES vTexture;  // android摄像头只能使用 samplerExternalOES类型纹理去接收摄像头画面

void main() {
    vec4 rgba = texture2D(vTexture, aCoord);
    gl_FragColor = vec4(rgba.r, rgba.g, rgba.b, rgba.a);
}