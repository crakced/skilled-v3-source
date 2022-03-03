 package me.vene.skilled.modules.mods.player;
 
import net.minecraft.client.Minecraft;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import net.minecraft.client.Minecraft;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 
public class InvMove extends Module
{
    private int left;
    private int right;
    private int backkey;
    private int forward;
    
    public InvMove() {
        super(StringRegistry.register("InvMove"), 0, Category.P);
        this.left = 30;
        this.right = 32;
        this.backkey = Minecraft.func_71410_x().field_71474_y.field_74368_y.func_151463_i();
        this.forward = Minecraft.func_71410_x().field_71474_y.field_74351_w.func_151463_i();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
    }
}
