precision mediump float;

varying vec2 aCoord;
uniform sampler2D vTexture;


void main() {
    float y = aCoord.y;

    // 取0.25 ~ 0.75 的区域进行绘制
    if (y < 0.5) {
        y += 0.25;
    } else {
        y -= 0.25;
    }

    gl_FragColor = texture2D(vTexture, vec2(aCoord.x, y));
}
