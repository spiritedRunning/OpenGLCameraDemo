precision mediump float;
varying vec2 aCoord;

uniform sampler2D vTexture;  // 图片
uniform vec2 left_eye;
uniform vec2 right_eye;

// r: 采集点与眼睛的距离
// rmax: 最大作用半径
// ratio: 放大系数 [-1, 1]
float fs(float r, float rmax, float ratio) {
    return (1.0 - pow(r / rmax - 1.0, 2.0) * ratio) * r;
}

vec2 newCoord(vec2 coord, vec2 eye, float rmax) {
    vec2 p = coord;
    // 采集点与眼睛eye中心点的距离
    float r = distance(coord, eye);

    if (r < rmax) {
        float fsr = fs(r, rmax, 0.3);  // 缩放后的点与眼睛的距离

        // (缩放后的点坐标 - 眼睛中心点坐标) / (采集点坐标 - 眼睛中心点坐标) = fsr / r
        // 缩放后的点坐标 = fsr/r * (采集点坐标 - 眼睛中心点坐标) + 眼睛中心点坐标
        p = fsr / r * (coord - eye) + eye;
    }
    return p;
}

void main() {
    float rmax = distance(left_eye, right_eye) / 2.0;
    vec2 p = newCoord(aCoord, left_eye, rmax); // 先和左眼比较
    p = newCoord(p, right_eye, rmax);  // 再和右眼比较

    gl_FragColor = texture2D(vTexture, p);
}