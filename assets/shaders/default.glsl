#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColour;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColour;
out vec2 fTexCoords;
out float fTexId;

void main()
{
    fColour = aColour;
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColour;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 colour;

void main()
{
    if (fTexId > 0) {
        int id = int(fTexId);
        colour = fColour * texture(uTextures[id], fTexCoords);
        //colour = vec4(fTexCoords, 0, 1);
    } else {
        colour = fColour;
    }
}