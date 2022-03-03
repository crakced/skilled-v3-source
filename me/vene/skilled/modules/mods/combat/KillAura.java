 package me.vene.skilled.modules.mods.combat;
 
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
 import java.lang.reflect.Field;
 import java.util.ArrayList;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.MathUtil;
 import me.vene.skilled.utilities.MouseUtil;
 import me.vene.skilled.utilities.ReflectionUtil;
 import me.vene.skilled.utilities.StringRegistry;
 import me.vene.skilled.values.BooleanValue;
 import me.vene.skilled.values.NumberValue;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.multiplayer.PlayerControllerMP;
 import net.minecraft.client.settings.KeyBinding;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.EntityLivingBase;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.ItemSword;
 import net.minecraft.network.Packet;
 import net.minecraft.network.play.client.C02PacketUseEntity;
 import net.minecraft.network.play.client.C03PacketPlayer;
 import net.minecraft.util.MathHelper;
 import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KillAura extends Module
{
    private boolean Blocking;
    private Minecraft mc;
    private BooleanValue mobs;
    private BooleanValue silent;
    private BooleanValue antiBot;
    private BooleanValue teams;
    private BooleanValue autoBlock;
    private Field damnfield;
    private NumberValue reachValue;
    private NumberValue minAps;
    private NumberValue maxAps;
    private ArrayList<EntityLivingBase> entityList;
    protected long lastMS;
    
    public KillAura() {
        super(StringRegistry.register("KillAura"), 0, Category.C);
        this.mc = Minecraft.func_71410_x();
        this.mobs = new BooleanValue("Mobs", false);
        this.silent = new BooleanValue("Silent", true);
        this.antiBot = new BooleanValue("Antibot", true);
        this.teams = new BooleanValue("Teams", true);
        this.autoBlock = new BooleanValue("Autoblock", false);
        this.reachValue = new NumberValue("Reach", 4.25, 3.0, 6.0);
        this.minAps = new NumberValue("Min APS", 7.0, 1.0, 20.0);
        this.maxAps = new NumberValue("Max APS", 14.0, 1.0, 20.0);
        this.entityList = new ArrayList<EntityLivingBase>();
        try {
            final String fieldshit = new String(new char[] { 'f', 'i', 'e', 'l', 'd', '_', '7', '8', '7', '7', '8', '_', 'j' });
            this.damnfield = PlayerControllerMP.class.getDeclaredField(StringRegistry.register(fieldshit));
        }
        catch (NoSuchFieldException ex) {}
        this.addValue(this.reachValue);
        this.addValue(this.maxAps);
        this.addValue(this.minAps);
        this.addOption(this.silent);
        this.addOption(this.autoBlock);
        this.addOption(this.mobs);
        this.addOption(this.teams);
    }
    
    public boolean isOnSameTeam(final EntityLivingBase entity) {
        if (entity.func_96124_cp() != null && this.mc.field_71439_g.func_96124_cp() != null) {
            final char c1 = entity.func_145748_c_().func_150254_d().charAt(1);
            final char c2 = this.mc.field_71439_g.func_145748_c_().func_150254_d().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    @Override
    public void onEnable() {
        this.updatebefore();
    }
    
    @Override
    public void onDisable() {
        if (this.Blocking && this.autoBlock.getState()) {
            final int useKeyBind = this.mc.field_71474_y.field_74313_G.func_151463_i();
            KeyBinding.func_74510_a(useKeyBind, false);
            MouseUtil.sendClick(1, false);
            this.Blocking = false;
        }
    }
    
    private boolean hasTimePassedMS(final long MS) {
        return this.getCurrentMS() >= this.lastMS + MS;
    }
    
    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    
    private void updatebefore() {
        this.lastMS = this.getCurrentMS();
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        this.entityList.clear();
        if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
            return;
        }
        for (final Object object : this.mc.field_71441_e.field_72996_f) {
            if (!(object instanceof EntityLivingBase) && this.mobs.getState()) {
                continue;
            }
            if (!(object instanceof EntityPlayer) && !this.mobs.getState()) {
                continue;
            }
            if (((EntityLivingBase)object).func_70032_d((Entity)this.mc.field_71439_g) > this.reachValue.getValue()) {
                continue;
            }
            if (object == this.mc.field_71439_g || ((EntityLivingBase)object).field_70128_L || ((EntityLivingBase)object).func_110143_aJ() <= 0.0f) {
                continue;
            }
            if (((EntityLivingBase)object).func_82150_aj()) {
                continue;
            }
            if (this.isOnSameTeam((EntityLivingBase)object) && this.teams.getState()) {
                continue;
            }
            this.entityList.add((EntityLivingBase)object);
        }
        if (!this.entityList.isEmpty()) {
            final EntityLivingBase target = this.entityList.get(0);
            if (this.autoBlock.getState() && this.mc.field_71439_g.func_71045_bC() != null && this.mc.field_71439_g.func_71045_bC().func_77973_b() instanceof ItemSword) {
                this.Blocking = true;
            }
            if (this.silent.getState()) {
                this.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook((float)(this.getRotationsNeeded((Entity)target)[0] + MathUtil.random.nextFloat() * 0.95 + 0.05), (float)(this.getRotationsNeeded((Entity)target)[1] + MathUtil.random.nextFloat() * 0.95 + 0.05), true));
            }
            else {
                this.mc.field_71439_g.field_70177_z = (float)(this.getRotationsNeeded((Entity)target)[0] + MathUtil.random.nextFloat() * 0.95 + 0.05);
                this.mc.field_71439_g.field_70125_A = (float)(this.getRotationsNeeded((Entity)target)[1] + MathUtil.random.nextFloat() * 0.95 + 0.05);
            }
            final double currentAps = MathUtil.random.nextFloat() * (this.maxAps.getValue() - this.minAps.getValue()) + this.minAps.getValue();
            if (this.hasTimePassedMS((int)Math.round(1000.0 / currentAps))) {
                this.mc.field_71439_g.func_71038_i();
                this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                this.updatebefore();
            }
        }
        else {
            this.Blocking = false;
        }
        this.checkBlock();
    }
    
    private void checkBlock() {
        if (this.Blocking) {
            final int useKeyBind = this.mc.field_71474_y.field_74313_G.func_151463_i();
            KeyBinding.func_74510_a(useKeyBind, true);
            KeyBinding.func_74507_a(useKeyBind);
            MouseUtil.sendClick(1, true);
        }
        else {
            final int useKeyBind = this.mc.field_71474_y.field_74313_G.func_151463_i();
            KeyBinding.func_74510_a(useKeyBind, false);
            MouseUtil.sendClick(1, false);
        }
    }
    
    private float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.field_70165_t - this.mc.field_71439_g.field_70165_t;
        final double diffZ = entity.field_70161_v - this.mc.field_71439_g.field_70161_v;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            diffY = entityLivingBase.field_70163_u + entityLivingBase.func_70047_e() * 0.6 - (this.mc.field_71439_g.field_70163_u + this.mc.field_71439_g.func_70047_e());
        }
        else {
            diffY = (entity.func_174813_aQ().field_72338_b + entity.func_174813_aQ().field_72337_e) / 2.0 - (this.mc.field_71439_g.field_70163_u + this.mc.field_71439_g.func_70047_e());
        }
        final double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { this.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - this.mc.field_71439_g.field_70177_z), this.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - this.mc.field_71439_g.field_70125_A) };
    }
    
    private boolean breakingBlock(final EntityPlayer player) {
        if (player instanceof EntityPlayerSP) {
            final PlayerControllerMP controller = Minecraft.func_71410_x().field_71442_b;
            final String fieldmeme = new String(new char[] { 'f', 'i', 'e', 'l', 'd', '_', '7', '8', '7', '7', '8', '_', 'j' });
            final boolean hasBlock = Boolean.valueOf(ReflectionUtil.getFieldValue(StringRegistry.register(fieldmeme), controller, PlayerControllerMP.class).toString());
            try {
                this.damnfield.setAccessible(true);
                this.damnfield.getBoolean(controller);
                this.damnfield.setAccessible(false);
            }
            catch (Exception ex) {}
            if (hasBlock) {
                return false;
            }
        }
        return true;
    }
}