// thomas diewald, 23.04.2012


#version 330
precision mediump float;

in vec3 VRY_VEC3_COLOR;
out vec4 VRY_FRAG_DATA;

void main(void)
{
  VRY_FRAG_DATA = vec4(VRY_VEC3_COLOR, 1.0);
}


