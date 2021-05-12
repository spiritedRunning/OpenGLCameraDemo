precision mediump float;

varying vec2 aCoord;
uniform sampler2D vTexture;


void main() {
    float y = aCoord.y;

    // 取1.0/3 ~ 2.0/3 的区域进行绘制
    float h = 1.0 / 3.0;
    if (y < h) {
        y += h;
    } else if (y > 2.0 * h) {
        y -= h;
    }

    gl_FragColor = texture2D(vTexture, vec2(aCoord.x, y));
}
