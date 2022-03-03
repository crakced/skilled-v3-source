 package me.vene.skilled.modules.mods.player;

import net.minecraft.client.Minecraft;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.values.NumberValue;
 import net.minecraft.client.Minecraft;
 import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Fly extends Module
{
    private Minecraft mc;
    private NumberValue speed;
    
    public Fly() {
        super("Fly", 0, Category.P);
        this.mc = Minecraft.func_71410_x();
        this.addValue(this.speed = new NumberValue("Speed", 3.0, 1.0, 10.0));
    }
    
    @Override
    public void onDisable() {
        if (this.mc.field_71439_g.field_71075_bZ.field_75100_b) {
            this.mc.field_71439_g.field_71075_bZ.field_75100_b = false;
        }
        this.mc.field_71439_g.field_71075_bZ.func_75092_a(0.05f);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.mc.field_71439_g.field_70181_x = 0.0;
        this.mc.field_71439_g.field_71075_bZ.func_75092_a(0.05f * (float)this.speed.getValue());
        this.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
    }
}
