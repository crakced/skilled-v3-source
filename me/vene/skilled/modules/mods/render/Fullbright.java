 package me.vene.skilled.modules.mods.render;
 
import net.minecraft.client.Minecraft;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import net.minecraft.client.Minecraft;
 
public class Fullbright extends Module
{
    private Minecraft mc;
    
    public Fullbright() {
        super(StringRegistry.register("Fullbright"), 0, Category.R);
        this.mc = Minecraft.func_71410_x();
    }
    
    @Override
    public void onEnable() {
        this.mc.field_71474_y.field_74333_Y = 1000.0f;
    }
    
    @Override
    public void onDisable() {
        this.mc.field_71474_y.field_74333_Y = 1.0f;
    }
}
