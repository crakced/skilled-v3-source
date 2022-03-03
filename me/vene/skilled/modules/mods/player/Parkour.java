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
 import net.minecraftforge.fml.common.gameevent.InputEvent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 import org.lwjgl.input.Keyboard;
 
public class Parkour extends Module
{
    private BlockPos currentBlock;
    private Minecraft mc;
    private static boolean jumpDown;
    
    public Parkour() {
        super(StringRegistry.register("Parkour"), 0, Category.P);
        this.mc = Minecraft.func_71410_x();
    }
    
    @SubscribeEvent
    public void onKeyPress(final InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(this.mc.field_71474_y.field_74314_A.func_151463_i()) && Minecraft.func_71410_x() != null && Minecraft.func_71410_x().field_71439_g != null && Minecraft.func_71410_x().field_71441_e != null) {
            Parkour.jumpDown = true;
        }
        else {
            Parkour.jumpDown = false;
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mc.field_71441_e != null) {
            if (this.mc.field_71439_g.field_70167_r - this.mc.field_71439_g.field_70163_u > 0.4) {
                return;
            }
            final int jumpKey = this.mc.field_71474_y.field_74314_A.func_151463_i();
            KeyBinding.func_74510_a(jumpKey, false);
            KeyBinding.func_74507_a(jumpKey);
            final double blockX = this.mc.field_71439_g.field_70165_t;
            final double blockY = this.mc.field_71439_g.field_70163_u - 1.0;
            final double blockZ = this.mc.field_71439_g.field_70161_v;
            final BlockPos thisBlock = new BlockPos(MathHelper.func_76128_c(blockX), MathHelper.func_76128_c(blockY), MathHelper.func_76128_c(blockZ));
            if (this.currentBlock == null || !this.isSameBlock(thisBlock, this.currentBlock)) {
                this.currentBlock = thisBlock;
            }
            if (this.mc.field_71441_e.func_175623_d(this.currentBlock) && this.mc.field_71439_g.field_70122_E && !this.mc.field_71439_g.func_70093_af() && !Parkour.jumpDown) {
                KeyBinding.func_74510_a(jumpKey, true);
                KeyBinding.func_74507_a(jumpKey);
            }
        }
    }
    
    private boolean isSameBlock(final BlockPos pos1, final BlockPos pos2) {
        return pos1.func_177958_n() == pos2.func_177958_n() && pos1.func_177956_o() == pos2.func_177956_o() && pos1.func_177952_p() == pos2.func_177952_p();
    }
    
    static {
        Parkour.jumpDown = false;
    }
}
