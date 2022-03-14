#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D AuraSampler;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main(){
    vec4 tex = texture(DiffuseSampler, texCoord);
    vec4 playerTex = texture(AuraSampler, texCoord);

    fragColor = tex + playerTex;
}