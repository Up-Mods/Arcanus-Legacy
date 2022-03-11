#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in vec2 UV1;
in vec2 UV2;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 EntityCentre;
uniform vec2 EntitySize;

uniform float STime;

out vec4 vertexColor;
out vec2 texCoord0;
out vec2 texCoord1;
out vec2 texCoord2;
out vec4 normal;
out vec3 position;

float calculateDistanceFactor(vec3 displacement) {
    return displacement.x;
}

void main() {
    float animation = STime * 2;

    float xs = sin(Position.x + animation);
    float zs = cos(Position.z + animation);

    gl_Position = ProjMat * ModelViewMat * (vec4(Position, 1.0) + vec4(xs / 32.0, 0.0, zs / 32.0, 0.0));

    vertexColor = Color;
    texCoord0 = UV0;
    texCoord1 = UV1;
    texCoord2 = UV2;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);

    position = Position + vec3(EntitySize.x, EntitySize.y, EntitySize.x);
    position = vec3(abs(position.x), abs(position.y), abs(position.z));
}
