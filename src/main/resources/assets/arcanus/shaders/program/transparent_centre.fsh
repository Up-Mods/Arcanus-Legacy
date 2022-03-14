#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Radius;

out vec4 fragColor;

void main(){
    vec4 centre = texture(DiffuseSampler, texCoord);
    float distanceToTranparency = 1.0;

    if (centre.a == 0) {
        fragColor = vec4(0);
        return;
    }

    for (float u = Radius; u >= 0; u -= 1.0) {
        for (float v = Radius; v >= 0; v -= 1.0) {
            float distance = sqrt(u * u + v * v) / (Radius);

            float s0 = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, -v * oneTexel.y)).a;
            float s1 = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, v * oneTexel.y)).a;
            float s2 = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, v * oneTexel.y)).a;
            float s3 = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, -v * oneTexel.y)).a;

            if (s0 <= 0 || s1 <= 0 || s2 <= 0 || s3 <= 0) {
                distanceToTranparency = distance;
            }
        }
    }

    fragColor = vec4(vec3(centre), centre.a * (1 - distanceToTranparency));
}
