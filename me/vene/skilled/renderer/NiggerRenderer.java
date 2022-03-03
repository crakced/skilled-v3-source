 package me.vene.skilled.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.modules.ModuleManager;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.EntityRenderer;
 import net.minecraft.client.resources.IResourceManager;
 
public class NiggerRenderer extends EntityRenderer
{
    public NiggerRenderer(final Minecraft minekraf, final IResourceManager resourceManager) {
        super(minekraf, resourceManager);
    }
    
    public void func_78473_a(final float dabfloat) {
        super.func_78473_a(dabfloat);
        for (final Module mod : ModuleManager.getModules()) {
            if (mod.getState()) {
                mod.onMouseOver(dabfloat);
            }
        }
    }
}
