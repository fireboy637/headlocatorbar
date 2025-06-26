package me.cortex.headlocatorbar.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import me.cortex.headlocatorbar.Headlocatorbar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocatorBarRenderer.class)
public class MixinLocatorBarRenderer {
    @Unique private ResourceLocation blitOverride;

    @Inject(method = "method_70870", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIIII)V",shift = At.Shift.BEFORE))
    private void injectRender(Level level, GuiGraphics guiGraphics, int i, TrackedWaypoint trackedWaypoint, CallbackInfo ci) {
        var lvl = Minecraft.getInstance().level;
        if (lvl != null && trackedWaypoint.id().left().isPresent()) {
            var entity = lvl.getEntity(trackedWaypoint.id().left().get());
            if (entity instanceof Player player) {
                this.blitOverride = Minecraft.getInstance().getSkinManager().getInsecureSkin(player.getGameProfile()).texture();
            }
        }
        trackedWaypoint.id();
    }

    @Redirect(method = "method_70870", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIIII)V"))
    private void redirectBlit(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation resourceLocation, int i, int j, int k, int l, int m) {
        if (this.blitOverride == null) {
            instance.blitSprite(renderPipeline, resourceLocation, i, j, k, l, m);
        } else {
            Headlocatorbar.doThing(instance, this.blitOverride, i, j, k, l,m);
        }
        this.blitOverride = null;
    }
}
