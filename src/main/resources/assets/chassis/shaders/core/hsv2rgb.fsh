#version 150

uniform vec4 ColorModulator; // Passed by user

in vec4 vertexColor;
out vec4 fragColor;

// https://github.com/hughsk/glsl-hsv2rgb/blob/master/index.glsl
vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    // Converting the input vertex color from hsv (hsb) to rgb, alpha (w) doesn't need conversion
    vec3 rgb = hsv2rgb(vertexColor.xyz);
    vec4 color = vec4(rgb.xyz, vertexColor.w);
    fragColor = color * ColorModulator;
}