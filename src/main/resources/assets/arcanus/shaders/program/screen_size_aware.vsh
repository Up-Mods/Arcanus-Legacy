#version 150

in vec4 Position;

uniform mat4 ProjMat;
uniform vec2 InSize;
uniform vec2 OutSize;
uniform vec2 BaseSize;

out vec2 texCoord;
out vec2 oneTexel;
out vec2 vertexPos;

void main(){
    vec4 outPos = ProjMat * vec4(Position.xy, 0.0, 1.0);
    gl_Position = vec4(outPos.xy, 0.2, 1.0);
    vertexPos = gl_Position;

    vec2 sizeFactor = InSize / BaseSize;
    oneTexel = sizeFactor / InSize;

    texCoord = Position.xy / OutSize;
}
