precision mediump float;
uniform sampler2D vTexture;
varying vec2 aCoord;

uniform float textureWidthOffset;
uniform float textureHeightOffset;

vec4 blurCoord[5];

// 1、进行模糊处理
void main() {
    // 偏移步距(0, 0.1)
    vec2 singleStepOffet = vec2(textureWidthOffset, textureHeightOffset);

    blurCoord[0] = vec4(aCoord - singleStepOffet, aCoord + singleStepOffet);
    blurCoord[1] = vec4(aCoord - 2.0 * singleStepOffet, aCoord + 2.0 * singleStepOffet);
    blurCoord[2] = vec4(aCoord - 3.0 * singleStepOffet, aCoord + 3.0 * singleStepOffet);
    blurCoord[3] = vec4(aCoord - 4.0 * singleStepOffet, aCoord + 4.0 * singleStepOffet);
    blurCoord[4] = vec4(aCoord - 5.0 * singleStepOffet, aCoord + 5.0 * singleStepOffet);

    // 计算当前坐标的颜色值
    vec4 currentColor = texture2D(vTexture, aCoord);
    vec3 sum = currentColor.rgb;

    // 计算偏移坐标的颜色值总和
    for (int i = 0; i < 5; i++) {
        sum += texture2D(vTexture, blurCoord[i].xy).rgb;
        sum += texture2D(vTexture, blurCoord[i].zw).rgb;
    }

    // 平均值 模糊效果
    vec4 blur = vec4(sum / 11.0, currentColor.a);
    gl_FragColor = blur;

}