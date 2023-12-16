package dev.cammiescorner.arcanus.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.cammiescorner.arcanus.Arcanus;
import dev.cammiescorner.arcanus.spell.Spell;
import dev.cammiescorner.arcanus.util.SpellBooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

    @Inject(method = "itemStackFromJson", at = @At("HEAD"), cancellable = true)
    private static void onParseItemStack(JsonObject stackObject, CallbackInfoReturnable<ItemStack> cir) {
        if(stackObject.has(SpellBooks.RECIPE_IDENTIFIER)) {
            String spellId = GsonHelper.getAsString(stackObject, SpellBooks.RECIPE_IDENTIFIER);
            Spell spell = Arcanus.SPELL.getOptional(ResourceLocation.tryParse(spellId))
                    .orElseThrow(() -> new JsonSyntaxException("[Arcanus] Unknown spell '" + spellId + "'"));

            ItemStack value = SpellBooks.getSpellBookWithAuthor(spell, null);
            cir.setReturnValue(value);
        }
    }
}
