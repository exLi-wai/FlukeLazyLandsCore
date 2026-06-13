package com.lw.flu.jei;

import com.lw.flu.FlukeLazyLandsCore;
import com.lw.flu.explosion.ExplosionRecipe;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class ExplosionRecipeCategory implements IRecipeCategory<ExplosionRecipe> {
    public static final RecipeType<ExplosionRecipe> TYPE = RecipeType.create(
            FlukeLazyLandsCore.MODID,
            "explosion",
            ExplosionRecipe.class
    );

    private final IDrawable background;
    private final IDrawable icon;

    public ExplosionRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(150, 72);
        this.icon = guiHelper.createDrawableItemLike(Items.TNT);
    }

    @Override
    public RecipeType<ExplosionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.flukelazylandscore.explosion");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ExplosionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 28)
                .setStandardSlotBackground()
                .addItemStack(recipe.inputStack());

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 43, 28)
                .addItemStack(new ItemStack(Items.TNT));

        int x = 76;
        int y = 6;
        for (int i = 0; i < recipe.drops().size(); i++) {
            var drop = recipe.drops().get(i);
            var chance = drop.chance();

            builder.addSlot(RecipeIngredientRole.OUTPUT, x + (i % 4) * 22, y + (i / 4) * 22)
                    .setOutputSlotBackground()
                    .addItemStack(drop.copyStack())
                    .addTooltipCallback((view, tooltip) -> {
                        if (chance < 1.0D) {
                            tooltip.add(Component.translatable("jei.flukelazylandscore.explosion.chance", Math.round(chance * 100.0D)));
                        }
                    });
        }
    }

    @Override
    public ResourceLocation getRegistryName(ExplosionRecipe recipe) {
        return recipe.id();
    }
}
