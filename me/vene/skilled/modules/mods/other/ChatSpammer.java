 package me.vene.skilled.modules.mods.other;

import net.minecraft.client.Minecraft;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import me.vene.skilled.values.NumberValue;
 import net.minecraft.client.Minecraft;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 
public class ChatSpammer extends Module
{
    protected long lastMS;
    private NumberValue delay;
    private Minecraft mc;
    
    private boolean hasTimePassedMS(final double MS) {
        return this.getCurrentMS() >= this.lastMS + MS;
    }
    
    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    private void updatebefore() {
        this.lastMS = this.getCurrentMS();
    }
    
    @Override
    public void onEnable() {
        this.updatebefore();
    }
    
    public ChatSpammer() {
        super(StringRegistry.register("Chat Spammer"), 0, Category.O);
        this.delay = new NumberValue("Delay", 5.0, 0.0, 60.0);
        this.mc = Minecraft.func_71410_x();
        this.addValue(this.delay);
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if ((this.mc.field_71439_g != null || this.mc.field_71441_e != null) && this.hasTimePassedMS(this.delay.getValue() * 1000.0)) {
            this.mc.field_71439_g.func_71165_d("");
        }
    }
}
