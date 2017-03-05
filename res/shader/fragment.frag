#version 120

//color passed through by vertex shader
varying vec4 colorv;

void main() {
    //set color as passed through
    gl_FragColor = colorv;
}