 package me.vene.skilled.modules.mods.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.realms.RealmsMth;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import net.minecraft.client.Minecraft;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Blocks;
 import net.minecraft.realms.RealmsMth;
 import net.minecraft.util.BlockPos;
 import net.minecraft.util.ChatComponentText;
 import net.minecraft.util.IChatComponent;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 

public class AutoDisconnect extends Module
{
    private BlockPos localBed;
    private Minecraft mc;
    
    public AutoDisconnect() {
        super("Auto Disconnect", 0, Category.U);
        this.localBed = null;
        this.mc = Minecraft.func_71410_x();
    }
    
    public static boolean bot(final Entity en) {
        if (en.func_70005_c_().startsWith("\u00c2�c")) {
            return true;
        }
        final String n = en.func_145748_c_().func_150260_c();
        if (n.contains("\u00c2�")) {
            return n.contains("[NPC] ");
        }
        if (n.isEmpty() && en.func_70005_c_().isEmpty()) {
            return true;
        }
        if (n.length() == 10) {
            int num = 0;
            int let = 0;
            final char[] charArray;
            final char[] var4 = charArray = n.toCharArray();
            for (final char c : charArray) {
                if (Character.isLetter(c)) {
                    if (Character.isUpperCase(c)) {
                        return false;
                    }
                    ++let;
                }
                else {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                    ++num;
                }
            }
            return num >= 2 && let >= 2;
        }
        return false;
    }
    
    public boolean isOnSameTeam(final EntityLivingBase entity) {
        if (entity.func_96124_cp() != null && this.mc.field_71439_g.func_96124_cp() != null) {
            final char c1 = entity.func_145748_c_().func_150254_d().charAt(1);
            final char c2 = this.mc.field_71439_g.func_145748_c_().func_150254_d().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    double distance(final double x, final double y) {
        return RealmsMth.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
    }
    
    double distance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return this.distance(y1 - y2, this.distance(x1 - x2, z1 - z2));
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Math.abs(this.mc.field_71439_g.field_70169_q - this.mc.field_71439_g.field_70165_t) > 10.0) {
            this.updateBed();
            this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("Teleport detected"));
        }
        if (this.localBed == null) {
            return;
        }
        this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("ALERT ASDFASDF"));
        for (final Object entity : this.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }
            if (bot((Entity)entity)) {
                this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("BOT ALERT ASDFASDF"));
            }
            else if (this.isOnSameTeam((EntityLivingBase)entity)) {
                this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("same team ALERT ASDFASDF"));
            }
            else {
                this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("SAME ASDFASDF"));
                if (this.distance(((EntityPlayer)entity).field_70165_t, ((EntityPlayer)entity).field_70163_u, ((EntityPlayer)entity).field_70161_v, this.localBed.func_177958_n(), this.localBed.func_177956_o(), this.localBed.func_177952_p()) > 30.0) {
                    this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("distanmce ALERT ASDFASDF"));
                }
                else {
                    this.mc.field_71439_g.func_71165_d("/l b");
                }
            }
        }
    }
    
    private void updateBed() {
        int ra;
        for (int y = ra = 30; y >= -ra; --y) {
            for (int x = -ra; x <= ra; ++x) {
                for (int z = -ra; z <= ra; ++z) {
                    final BlockPos p = new BlockPos(this.mc.field_71439_g.field_70165_t + x, this.mc.field_71439_g.field_70163_u + y, this.mc.field_71439_g.field_70161_v + z);
                    final boolean bed = this.mc.field_71441_e.func_180495_p(p).func_177230_c() == Blocks.field_150324_C;
                    if (bed) {
                        this.localBed = p;
                        this.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("FOUND BED"));
                        break;
                    }
                }
            }
        }
    }
}
