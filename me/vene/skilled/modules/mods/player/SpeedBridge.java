 package me.vene.skilled.modules.mods.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import me.vene.skilled.values.BooleanValue;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.settings.KeyBinding;
 import net.minecraft.entity.Entity;
 import net.minecraft.item.ItemBlock;
 import net.minecraft.network.Packet;
 import net.minecraft.network.play.client.C0BPacketEntityAction;
 import net.minecraft.util.BlockPos;
 import net.minecraft.util.MathHelper;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 
public class SpeedBridge extends Module
{
    private boolean sneaking;
    private boolean safewalkSneaking;
    private boolean playerIsSneaking;
    private BlockPos currentBlock;
    private Minecraft mc;
    private BooleanValue onlyBlocks;
    private BlockPos lastBlock;
    
    public SpeedBridge() {
        super(StringRegistry.register("Speed Bridge"), 0, Category.P);
        this.sneaking = false;
        this.safewalkSneaking = false;
        this.playerIsSneaking = false;
        this.mc = Minecraft.func_71410_x();
        this.addOption(this.onlyBlocks = new BooleanValue(StringRegistry.register("Only Blocks"), false));
    }
    
    @Override
    public void onDisable() {
        KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), false);
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
            if (this.onlyBlocks.getState() && (this.mc.field_71439_g.func_71045_bC() == null || !(this.mc.field_71439_g.func_71045_bC().func_77973_b() instanceof ItemBlock))) {
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
                this.setSafewalkSneaking(true);
            }
            else {
                this.setSafewalkSneaking(false);
            }
            this.checkSneak();
        }
    }
    
    private void doSafewalk() {
        if (this.isSneaking() != this.mc.field_71439_g.func_70093_af()) {
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), this.isSneaking());
        }
    }
    
    private boolean isSneaking() {
        return this.sneaking;
    }
    
    private boolean isSameBlock(final BlockPos pos1, final BlockPos pos2) {
        return pos1.func_177958_n() == pos2.func_177958_n() && pos1.func_177956_o() == pos2.func_177956_o() && pos1.func_177952_p() == pos2.func_177952_p();
    }
    
    public void setSafewalkSneaking(final boolean safewalkSneaking) {
        this.safewalkSneaking = safewalkSneaking;
    }
    
    private void checkSneak() {
        if (!this.playerIsSneaking) {
            if (this.mc.field_71439_g.field_70122_E) {
                this.sneaking = this.safewalkSneaking;
                this.doSafewalk();
            }
            else {
                KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), false);
            }
        }
        else {
            KeyBinding.func_74510_a(this.mc.field_71474_y.field_74311_E.func_151463_i(), true);
        }
    }
    
    public void setFakedSneakingState(final boolean sneaking) {
        if (this.mc.field_71439_g != null) {
            final C0BPacketEntityAction.Action action = sneaking ? C0BPacketEntityAction.Action.START_SNEAKING : C0BPacketEntityAction.Action.STOP_SNEAKING;
            this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this.mc.field_71439_g, action));
            this.mc.field_71439_g.field_71158_b.field_78899_d = sneaking;
        }
    }
}
