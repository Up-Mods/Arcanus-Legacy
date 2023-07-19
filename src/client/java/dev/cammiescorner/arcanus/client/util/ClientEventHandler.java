package dev.cammiescorner.arcanus.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.client.ArcanusClient;
import dev.cammiescorner.arcanus.common.items.WandItem;
import dev.cammiescorner.arcanus.common.packets.CastSpellPacket;
import dev.cammiescorner.arcanus.core.util.ArcanusHelper;
import dev.cammiescorner.arcanus.core.util.MagicUser;
import dev.cammiescorner.arcanus.core.util.Spell;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClientEventHandler {
    private static final Identifier HUD_ELEMENTS = new Identifier(Arcanus.MOD_ID, "textures/gui/hud_elements.png");

    public static void clientEvents() {
        final MinecraftClient client = MinecraftClient.getInstance();
        var manaTimer = new Object() {
            int value;
        };

        //-----HUD Render Callback-----//
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            if(client.cameraEntity instanceof PlayerEntity player && !player.isSpectator() && !player.isCreative()) {
                MagicUser user = (MagicUser) player;
                int mana = Math.min(user.getMana(), user.getMaxMana() - user.getBurnout());
                int burnout = user.getBurnout();
                int manaLock = ArcanusHelper.getManaLock(player);

                if(player.getMainHandStack().getItem() instanceof WandItem || mana < user.getMaxMana())
                    manaTimer.value = Math.min(manaTimer.value + 1, 40);
                else
                    manaTimer.value = Math.max(manaTimer.value - 1, 0);

                if(manaTimer.value > 0) {
                    user.shouldShowMana(true);
                    int scaledWidth = client.getWindow().getScaledWidth();
                    int scaledHeight = client.getWindow().getScaledHeight();
                    int x = scaledWidth / 2 + 82;
                    int y = scaledHeight - (player.isCreative() ? 34 : 49);
                    float alpha = manaTimer.value > 20 ? 1F : manaTimer.value / 20F;

                    RenderSystem.enableBlend();
                    RenderSystem.setShaderTexture(0, HUD_ELEMENTS);
                    RenderSystem.setShaderColor(1F, 1F, 1F, alpha);

                    // Draw background
                    for(int i = 0; i < 10; i++)
                        DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 15, 9, 9, 256, 256);

                    // Draw full mana orb
                    for(int i = 0; i < mana / 2; i++)
                        DrawableHelper.drawTexture(matrices, x - (i * 8), y, 0, 0, 8, 8, 256, 256);

                    // Draw half mana orb
                    if(mana % 2 == 1)
                        DrawableHelper.drawTexture(matrices, x - ((mana / 2) * 8), y, 8, 0, 8, 8, 256, 256);

                    boolean manaLockOdd = manaLock % 2 == 1;
                    boolean burnoutOdd = burnout % 2 == 1;
                    int adjustedBurnout = manaLockOdd && burnoutOdd ? burnout / 2 + 1 : burnout / 2;
                    int adjustedManaLock = (manaLock / 2) * 8;
                    x -= 72;

                    // Draw full burnout orb
                    for(int i = 0; i < adjustedBurnout; i++)
                        if(manaLockOdd && i == 0)
                            DrawableHelper.drawTexture(matrices, x + adjustedManaLock, y, 32, 0, 8, 8, 256, 256);
                        else
                            DrawableHelper.drawTexture(matrices, x + adjustedManaLock + (i * 8), y, 16, 0, 8, 8, 256, 256);

                    // Draw half burnout orb
                    if(burnoutOdd != manaLockOdd && burnout > 0)
                        DrawableHelper.drawTexture(matrices, x + adjustedManaLock + (adjustedBurnout * 8), y, 24, 0, 8, 8, 256, 256);

                    // Draw full mana lock orb
                    for(int i = 0; i < manaLock / 2; i++)
                        DrawableHelper.drawTexture(matrices, x + (i * 8), y, 40, 0, 8, 8, 256, 256);

                    // Draw half mana lock orb
                    if(manaLock % 2 == 1)
                        DrawableHelper.drawTexture(matrices, x + adjustedManaLock, y, 48, 0, 8, 8, 256, 256);
                }
                else
                    user.shouldShowMana(false);
            }
        });

        ClientTickEvents.START_CLIENT_TICK.register(client1 -> {
            if (ArcanusClient.timer == 0 && !ArcanusClient.pattern.isEmpty())
                ArcanusClient.pattern.clear();

            if (client1.player == null)
                return;

            if (client1.player.getMainHandStack().getItem() instanceof WandItem) {
                if (ArcanusClient.timer > 0) {
                    MutableText hyphen = new LiteralText("-").formatted(Formatting.GRAY);

                    client1.player.sendMessage(Arcanus.getSpellInputs(ArcanusClient.pattern, 0).append(hyphen).append(Arcanus.getSpellInputs(ArcanusClient.pattern, 1)).append(hyphen).append(Arcanus.getSpellInputs(ArcanusClient.pattern, 2)), true);

                    if (ArcanusClient.pattern.size() == 3) {
                        for (Spell spell : Arcanus.SPELL) {
                            if (ArcanusClient.pattern.equals(spell.getSpellPattern())) {
                                CastSpellPacket.send(Arcanus.SPELL.getRawId(spell));
                                ArcanusClient.unfinishedSpell = false;
                                break;
                            } else {
                                if (Arcanus.SPELL.getRawId(spell) + 1 == Arcanus.SPELL.getIds().size()) {
                                    client1.player.sendMessage(new TranslatableText("error." + Arcanus.MOD_ID + ".missing_spell").formatted(Formatting.RED), true);
                                    ArcanusClient.unfinishedSpell = false;
                                }
                            }
                        }

                        ArcanusClient.timer = 0;
                    }
                } else if (ArcanusClient.pattern.size() < 3 && ArcanusClient.unfinishedSpell)
                    client1.player.sendMessage(new LiteralText(""), true);
            } else
                ArcanusClient.timer = 0;

            if (ArcanusClient.timer > 0)
                ArcanusClient.timer--;
        });
    }
}
