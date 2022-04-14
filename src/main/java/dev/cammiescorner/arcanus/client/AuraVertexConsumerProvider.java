package dev.cammiescorner.arcanus.client;

import net.minecraft.client.render.FixedColorVertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;

/**
 * A {@link VertexConsumerProvider} which provides vertex consumers which render auras. It only renders for render layers
 * which affect the outline of an entity.
 *
 * @see net.minecraft.client.render.OutlineVertexConsumerProvider
 */
public final class AuraVertexConsumerProvider implements VertexConsumerProvider {
    private final VertexConsumerProvider provider;
    private final int r;
    private final int g;
    private final int b;
    private final int a;

    /**
     * Create a new AuraVertexConsumerProvider with a given base VertexConsumerProvider and aura colour.
     *
     * @param provider  the base VertexConsumerProvider
     * @param r         the aura's red component
     * @param g         the aura's green component
     * @param b         the aura's blue component
     * @param a         the aura's alpha component
     */
    public AuraVertexConsumerProvider(VertexConsumerProvider provider, int r, int g, int b, int a) {
        this.provider = provider;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Gets a vertex consumer which renders an aura with the default texture.
     *
     * @return the vertex consumer
     */
    public VertexConsumer getBuffer() {
        return new AuraVertexConsumer(provider.getBuffer(AuraEffectManager.getRenderLayer()), r, g, b, a);
    }

    /**
     * Gets a vertex consumer which renders an aura with a given texture.
     *
     * @param texture the texture
     *
     * @return the vertex consumer
     */
    public VertexConsumer getBuffer(Identifier texture) {
        return new AuraVertexConsumer(provider.getBuffer(AuraEffectManager.getRenderLayer(texture)), r, g, b, a);
    }

    @Override
    public VertexConsumer getBuffer(RenderLayer layer) {
        if (layer.getAffectedOutline().isPresent()) {
            return new AuraVertexConsumer(provider.getBuffer(AuraEffectManager.getRenderLayer(layer)), r, g, b, a);
        } else {
            return new DummyVertexConsumer();
        }
    }

    /**
     * A dummy vertex consumer which does nothing. Used for render layers which do not affect the outline of an entity.
     */
    private final static class DummyVertexConsumer implements VertexConsumer {
        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer texture(float u, float v) {
            return this;
        }

        @Override
        public VertexConsumer overlay(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer light(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        @Override
        public void next() {
        }

        @Override
        public void fixedColor(int red, int green, int blue, int alpha) {
        }

        @Override
        public void unfixColor() {
        }
    }

    /**
     * The aura vertex consumer.
     */
    private final static class AuraVertexConsumer extends FixedColorVertexConsumer {
        private final VertexConsumer delegate;
        private double x;
        private double y;
        private double z;
        private float u;
        private float v;

        AuraVertexConsumer(VertexConsumer delegate, int red, int green, int blue, int alpha) {
            this.delegate = delegate;
            super.fixedColor(red, green, blue, alpha);
        }

        @Override
        public void fixedColor(int red, int green, int blue, int alpha) {
        }

        @Override
        public void unfixColor() {
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return this;
        }

        @Override
        public VertexConsumer texture(float u, float v) {
            this.u = u;
            this.v = v;
            return this;
        }

        @Override
        public VertexConsumer overlay(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer light(int u, int v) {
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        @Override
        public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
            this.delegate.vertex(x, y, z).color(this.fixedRed, this.fixedGreen, this.fixedBlue, this.fixedAlpha).texture(u, v).next();
        }

        @Override
        public void next() {
            this.delegate.vertex(this.x, this.y, this.z).color(this.fixedRed, this.fixedGreen, this.fixedBlue, this.fixedAlpha).texture(this.u, this.v).next();
        }
    }
}
