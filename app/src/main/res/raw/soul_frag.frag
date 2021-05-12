
varying highp vec2 aCoord;

uniform sampler2D vTexture;
uniform lowp float mixturePercent;
uniform highp float scalePercent;   // 放大参数, 大于1 用于扩大

void main() {
    lowp vec4 textureColor = texture2D(vTexture, aCoord);
    highp vec2 textureCoordinateToUse = aCoord;

    // 纹理中心点
    highp vec2 center = vec2(0.5, 0.5);
    textureCoordinateToUse -= center;
    textureCoordinateToUse = textureCoordinateToUse / scalePercent;
    textureCoordinateToUse += center;

    lowp vec4 textureColor2 = texture2D(vTexture, textureCoordinateToUse);

    gl_FragColor = mix(textureColor, textureColor2, mixturePercent);

}