#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float Radius;
uniform float StepGranularity;
uniform mat4 PerspectiveMat;

out vec4 fragColor;

float calcDistance(in float windowZ) {
    float ndcZ = (2.0 * windowZ - gl_DepthRange.near - gl_DepthRange.far) / (gl_DepthRange.far - gl_DepthRange.near);
    return PerspectiveMat[3][2] / ((PerspectiveMat[2][3] * ndcZ) - PerspectiveMat[2][2]);
}

void main(){
    vec4 centre = texture(DiffuseSampler, texCoord);
    vec4 maxVal = centre;

    float step = max(1, ceil(Radius/StepGranularity));
    for(float u = 0.0; u <= Radius; u += step) {
        for(float v = 0.0; v <= Radius; v += step) {
            if (maxVal.a <= 0) {
                float weight = (((sqrt(u * u + v * v) / (Radius)) > 1.0) ? 0.0 : 1.0);

                vec4 leftDown = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, -v * oneTexel.y));
                leftDown.a *= 10.0 - calcDistance(texture(DepthSampler, texCoord + vec2(-u * oneTexel.x, -v * oneTexel.y)).x);
                vec4 rightUp = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, v * oneTexel.y));
                rightUp.a *= 10.0 - calcDistance(texture(DepthSampler, texCoord + vec2(u * oneTexel.x, v * oneTexel.y)).x);
                vec4 rightDown = texture(DiffuseSampler, texCoord + vec2(-u * oneTexel.x, v * oneTexel.y));
                rightDown.a *= 10.0 - calcDistance(texture(DepthSampler, texCoord + vec2(-u * oneTexel.x, v * oneTexel.y)).x);
                vec4 leftUp = texture(DiffuseSampler, texCoord + vec2(u * oneTexel.x, -v * oneTexel.y));
                leftUp.a *= 10.0 - calcDistance(texture(DepthSampler, texCoord + vec2(u * oneTexel.x, -v * oneTexel.y)).x);

                vec4 tmpMax0 = max(leftDown, rightUp);
                vec4 tmpMax1 = max(rightDown, leftUp);
                vec4 tempMax2 = max(tmpMax0, tmpMax1);
                maxVal = mix(maxVal, max(maxVal, tempMax2), weight);
            }
        }
    }

    maxVal.r /= 10.0;

    fragColor = maxVal;
}
