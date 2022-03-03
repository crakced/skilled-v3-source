 package me.vene.skilled.modules.mods.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import me.vene.skilled.values.BooleanValue;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.init.Blocks;
 import net.minecraft.potion.Potion;
 import net.minecraft.util.BlockPos;
 import net.minecraft.util.MathHelper;
 import net.minecraft.util.MovingObjectPosition;
 import net.minecraftforge.client.event.MouseEvent;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 import org.lwjgl.input.Mouse;
 
public class BlockFly extends Module
{
    private boolean sneaking;
    private boolean safewalkSneaking;
    private boolean playerIsSneaking;
    private BlockPos currentBlock;
    private Minecraft mc;
    private BlockPos lastBlock;
    private BooleanValue clearBlocks;
    
    public BlockFly() {
        super(StringRegistry.register("BlockFly"), 0, Category.P);
        this.sneaking = false;
        this.safewalkSneaking = false;
        this.playerIsSneaking = false;
        this.mc = Minecraft.func_71410_x();
        this.lastBlock = null;
        this.clearBlocks = new BooleanValue(StringRegistry.register("Clear Blocks"), false);
    }
    
    public void swing() {
        final EntityPlayerSP p = this.mc.field_71439_g;
        final int armSwingEnd = p.func_70644_a(Potion.field_76422_e) ? (6 - (1 + p.func_70660_b(Potion.field_76422_e).func_76458_c())) : (p.func_70644_a(Potion.field_76419_f) ? (6 + (1 + p.func_70660_b(Potion.field_76419_f).func_76458_c()) * 2) : 6);
        if (!p.field_82175_bq || p.field_110158_av >= armSwingEnd / 2 || p.field_110158_av < 0) {
            p.field_110158_av = -1;
            p.field_82175_bq = true;
        }
    }
    
    @Override
    public void onDisable() {
        if (this.lastBlock != null) {
            this.mc.field_71441_e.func_175656_a(this.lastBlock, Blocks.field_150350_a.func_176223_P());
        }
    }
    
    @SubscribeEvent
    public void m(final MouseEvent e) {
        if (e.buttonstate && (e.button == 0 || e.button == 1)) {
            final MovingObjectPosition mop = this.mc.field_71476_x;
            if (mop != null && mop.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
                final int x = mop.func_178782_a().func_177958_n();
                final int z = mop.func_178782_a().func_177952_p();
                final BlockPos pos = this.currentBlock;
                if (pos.func_177958_n() == x && pos.func_177952_p() == z) {
                    e.setCanceled(true);
                    if (e.button == 0) {
                        this.swing();
                    }
                    Mouse.poll();
                }
            }
        }
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.mc.field_71441_e != null) {
            if (this.mc.field_71439_g.field_70167_r - this.mc.field_71439_g.field_70163_u > 0.4) {
                return;
            }
            if (this.mc.field_71439_g.field_71075_bZ.field_75100_b) {
                return;
            }
            final double blockX = this.mc.field_71439_g.field_70165_t;
            final double blockY = this.mc.field_71439_g.field_70163_u - 1.0;
            final double blockZ = this.mc.field_71439_g.field_70161_v;
            final BlockPos thisBlock = new BlockPos(MathHelper.func_76128_c(blockX), MathHelper.func_76128_c(blockY), MathHelper.func_76128_c(blockZ));
            if (this.currentBlock == null || !this.isSameBlock(thisBlock, this.currentBlock)) {
                this.currentBlock = thisBlock;
            }
            if (this.mc.field_71441_e.func_175623_d(this.currentBlock)) {
                this.mc.field_71441_e.func_175656_a(thisBlock, Blocks.field_180401_cv.func_176223_P());
                if (this.lastBlock != null) {
                    this.mc.field_71441_e.func_175656_a(this.lastBlock, Blocks.field_150350_a.func_176223_P());
                }
                this.lastBlock = thisBlock;
            }
        }
    }
    
    private boolean isSameBlock(final BlockPos pos1, final BlockPos pos2) {
        return pos1.func_177958_n() == pos2.func_177958_n() && pos1.func_177956_o() == pos2.func_177956_o() && pos1.func_177952_p() == pos2.func_177952_p();
    }
}
