// thomas diewald, 23.04.2012

#version 330
precision mediump float;

in vec3 IN_VEC3_POSITION;
in vec3 IN_VEC3_COLOR;

uniform mat4 UN_MAT4_PROJECTION;
uniform mat4 UN_MAT4_MODELVIEW;

out vec3 VRY_VEC3_COLOR;

void main(void)
{
  VRY_VEC3_COLOR = vec3(1, 1, 1); //IN_VEC3_COLOR;
  //VRY_VEC3_COLOR = IN_VEC3_COLOR;
  gl_Position    = UN_MAT4_PROJECTION * UN_MAT4_MODELVIEW * vec4(IN_VEC3_POSITION, 1.0);
}

