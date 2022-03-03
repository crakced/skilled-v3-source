 package me.vene.skilled.modules.mods.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.settings.KeyBinding;
 import net.minecraft.util.BlockPos;
 import net.minecraft.util.MathHelper;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 
public class Safewalk extends Module
{
    private Minecraft mc;
    private static boolean c;
    private static boolean d;
    
    public Safewalk() {
        super(StringRegistry.register("Safewalk"), 0, Category.P);
        this.mc = Minecraft.func_71410_x();
    }
    
    @SubscribeEvent
    public void p(final TickEvent.PlayerTickEvent e) {
        if (!this.e()) {
            return;
        }
        if (this.mc.field_71439_g.field_70122_E) {
            if (this.eob()) {
                this.sh(Safewalk.d = true);
                Safewalk.c = true;
            }
            else if (Safewalk.d) {
                this.sh(Safewalk.d = false);
            }
        }
        if (Safewalk.c && this.mc.field_71439_g.field_71075_bZ.field_75100_b) {
            this.sh(false);
            Safewalk.c = false;
        }
    }
    
    private void sh(final boolean sh) {
        KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), sh);
    }
    
    public boolean e() {
        return this.mc.field_71439_g != null && this.mc.field_71441_e != null;
    }
    
    public boolean eob() {
        final double x = this.mc.field_71439_g.field_70165_t;
        final double y = this.mc.field_71439_g.field_70163_u - 1.0;
        final double z = this.mc.field_71439_g.field_70161_v;
        final BlockPos p = new BlockPos(MathHelper.func_76128_c(x), MathHelper.func_76128_c(y), MathHelper.func_76128_c(z));
        return this.mc.field_71441_e.func_175623_d(p);
    }
    
    static {
        Safewalk.c = false;
        Safewalk.d = false;
    }
}
