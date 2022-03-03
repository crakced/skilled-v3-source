 package me.vene.skilled.modules.mods.combat;
 
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.settings.KeyBinding;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 
public class Sprint extends Module
{
    private Minecraft mc;
    
    public Sprint() {
        super(StringRegistry.register("Sprint"), 0, Category.C);
        this.mc = Minecraft.func_71410_x();
    }
    
    @Override
    public void onEnable() {
        final int forwardKey = this.mc.field_71474_y.field_151444_V.func_151463_i();
        KeyBinding.func_74510_a(forwardKey, true);
        KeyBinding.func_74507_a(forwardKey);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mc.field_71439_g != null && this.mc.field_71441_e != null) {
            final int forwardKey = this.mc.field_71474_y.field_151444_V.func_151463_i();
            KeyBinding.func_74510_a(forwardKey, true);
            KeyBinding.func_74507_a(forwardKey);
        }
    }
    
    @Override
    public void onDisable() {
        final int forwardKey = this.mc.field_71474_y.field_151444_V.func_151463_i();
        KeyBinding.func_74510_a(forwardKey, false);
        KeyBinding.func_74507_a(forwardKey);
    }
}
