#version 120

//the position of the vertex as specified by our renderer
attribute vec3 Position;
//the color of the vertex as specified by renderer
varying vec4 colorv;

void main() {
    //pass along the position
    gl_Position = vec4(Position, 1.0);
    //pass along the color
    colorv = gl_Color;
}