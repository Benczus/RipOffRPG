#version 120

attribute vec3 vertices;  // vertex shader only!
attribute vec2 textures;

varying vec2 tex_coords;                // varying variable that fragment shader can use !

uniform mat4 projection;     // matrix 4x4 float

void main(){
tex_coords=textures;
 gl_Position = projection * vec4(vertices, 1);

}