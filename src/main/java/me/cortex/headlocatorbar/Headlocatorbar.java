package me.cortex.headlocatorbar;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class Headlocatorbar {
    public static void doThing(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, int k, int l, int m) {
        float U0 = 1f/8;
        float V0 = 1f/8;
        float U1 = 2f/8;
        float V1 = 2f/8;
        instance.blit(resourceLocation, i, j, i+k, j+ l, U0,U1,V0,V1);
    }
}
