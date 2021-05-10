precision mediump float;
uniform sampler2D vTexture;
varying vec2 aCoord;

uniform sampler2D vBlurTexture;

// 2、高反差保留 边缘锐化
void main() {

    vec4 currentColor = texture2D(vTexture, aCoord);
    vec4 blurColor = texture2D(vBlurTexture, aCoord);
    // 高反差 = 原图 - 高斯模糊图
    vec4 highPassColor = currentColor - blurColor;

    float intensity = 24.0;  // 强光程度
    // color = 2 * color1 * color2;+
    // clamp: 获得三个参数中大小处在中间的那个值*
    highPassColor.r = clamp(2.0 * highPassColor.r * highPassColor.r * intensity, 0.0, 1.0);
    highPassColor.g = clamp(2.0 * highPassColor.g * highPassColor.g * intensity, 0.0, 1.0);
    highPassColor.b = clamp(2.0 * highPassColor.b * highPassColor.b * intensity, 0.0, 1.0);

    vec4 highPassBlur = vec4(highPassColor.rgb, 1.0);
    gl_FragColor = highPassBlur;
}