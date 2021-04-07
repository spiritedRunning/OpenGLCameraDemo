attribute vec4 vPosition;  // 由java传入
attribute vec2 vCoord; // 纹理坐标

varying vec2 aCoord;  // 传递给片元着色器
uniform mat4 vMatrix;

void main() {
    gl_Position = vPosition;
    aCoord = (vMatrix * vec4(vCoord, 1.0, 1.0)).xy;
}

