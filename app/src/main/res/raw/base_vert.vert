attribute vec4 vPosition;  // 由java传入
attribute vec2 vCoord; // 纹理坐标

varying vec2 aCoord;  // 传递给片元着色器


void main() {
    gl_Position = vPosition;
    aCoord = vCoord;
}

