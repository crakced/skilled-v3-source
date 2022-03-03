 package me.vene.skilled.modules.mods.combat;
 
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
 import java.lang.reflect.Field;
 import java.lang.reflect.Method;
 import java.util.List;
 import java.util.Random;
 import java.util.concurrent.CopyOnWriteArrayList;
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
 import net.minecraft.client.gui.GuiScreen;
 import net.minecraft.client.gui.inventory.GuiChest;
 import net.minecraft.client.gui.inventory.GuiInventory;
 import net.minecraft.client.multiplayer.PlayerControllerMP;
 import net.minecraft.client.settings.KeyBinding;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.item.Item;
 import net.minecraft.item.ItemAxe;
 import net.minecraft.item.ItemSword;
 import net.minecraftforge.fml.common.gameevent.TickEvent;
 import org.lwjgl.input.Keyboard;
 import org.lwjgl.input.Mouse;
 
public class AutoClicker extends Module
{
    private long nextLeftUp;
    private long nextLeftDown;
    private long nextDrop;
    private long nextExhaust;
    private double dropRate;
    private boolean dropping;
    private Random random;
    private Method guiScreenMethod;
    private String MinCPS;
    private String MaxCPS;
    private String jitterIntensity;
    private String inventory;
    private String jitt;
    private String swords;
    private String breakblocks;
    private NumberValue mincps;
    private NumberValue maxcps;
    private NumberValue jitterstren;
    private BooleanValue jitter;
    private BooleanValue weapon;
    private BooleanValue inv;
    private BooleanValue breakOption;
    private BooleanValue limitItems;
    private BooleanValue blockHit;
    private boolean doBlockHit;
    private Minecraft mc;
    private Field damnfield;
    public List<Item> limitList;
    private boolean removeThings;
    
    public AutoClicker() {
        super(StringRegistry.register(new String(new char[] { 'A', 'u', 't', 'o', 'c', 'l', 'i', 'c', 'k', 'e', 'r' })), 0, Category.C);
        this.random = new Random();
        this.MinCPS = StringRegistry.register(new String(new char[] { 'M', 'i', 'n', ' ', 'C', 'P', 'S' }));
        this.MaxCPS = StringRegistry.register(new String(new char[] { 'M', 'a', 'x', ' ', 'C', 'P', 'S' }));
        this.jitterIntensity = StringRegistry.register(new String(new char[] { 'J', 'i', 't', 't', 'e', 'r', ' ', 'V', 'a', 'l', 'u', 'e' }));
        this.inventory = StringRegistry.register(new String(new char[] { 'I', 'n', 'v', 'e', 'n', 't', 'o', 'r', 'y', ' ', 'F', 'i', 'l', 'l' }));
        this.jitt = StringRegistry.register(new String(new char[] { 'J', 'i', 't', 't', 'e', 'r' }));
        this.swords = StringRegistry.register(new String(new char[] { 'O', 'n', 'l', 'y', ' ', 's', 'w', 'o', 'r', 'd', 's', '/', 'a', 'x', 'e', 's' }));
        this.breakblocks = StringRegistry.register(new String(new char[] { 'B', 'r', 'e', 'a', 'k', ' ', 'b', 'l', 'o', 'c', 'k', 's' }));
        this.mincps = new NumberValue(StringRegistry.register(this.MinCPS), 10.0, 1.0, 20.0);
        this.maxcps = new NumberValue(StringRegistry.register(this.MaxCPS), 15.0, 1.0, 20.0);
        this.jitterstren = new NumberValue(StringRegistry.register(this.jitterIntensity), 0.0, 0.1, 2.0);
        this.jitter = new BooleanValue(StringRegistry.register(this.jitt), false);
        this.weapon = new BooleanValue(StringRegistry.register(this.swords), true);
        this.inv = new BooleanValue(StringRegistry.register(this.inventory), true);
        this.breakOption = new BooleanValue(StringRegistry.register(this.breakblocks), true);
        this.limitItems = new BooleanValue(StringRegistry.register("Limit to items"), false);
        this.blockHit = new BooleanValue(StringRegistry.register("Blockhit"), true);
        this.doBlockHit = false;
        this.mc = Minecraft.func_71410_x();
        this.limitList = new CopyOnWriteArrayList<Item>();
        this.removeThings = false;
        this.addValue(this.mincps);
        this.addValue(this.maxcps);
        this.addValue(this.jitterstren);
        this.addOption(this.jitter);
        this.addOption(this.inv);
        this.addOption(this.weapon);
        this.addOption(this.breakOption);
        this.addOption(this.limitItems);
        this.addOption(this.blockHit);
        try {
            final String funcshit = new String(new char[] { 'f', 'u', 'n', 'c', '_', '7', '3', '8', '6', '4', '_', 'a' });
            this.guiScreenMethod = GuiScreen.class.getDeclaredMethod(StringRegistry.register(funcshit), Integer.TYPE, Integer.TYPE, Integer.TYPE);
        }
        catch (NoSuchMethodException ex) {}
        try {
            final String fieldshit = new String(new char[] { 'f', 'i', 'e', 'l', 'd', '_', '7', '8', '7', '7', '8', '_', 'j' });
            this.damnfield = PlayerControllerMP.class.getDeclaredField(StringRegistry.register(fieldshit));
        }
        catch (NoSuchFieldException ex2) {}
    }
    
    private boolean check(final EntityPlayerSP playerSP) {
        return !this.weapon.getState() || (playerSP.func_71045_bC() != null && (playerSP.func_71045_bC().func_77973_b() instanceof ItemSword || playerSP.func_71045_bC().func_77973_b() instanceof ItemAxe));
    }
    
    private boolean check2(final EntityPlayerSP playerSP) {
        return playerSP.func_71045_bC() != null && this.limitList.contains(playerSP.func_71045_bC().func_77973_b());
    }
    
    public void addToList(final String itemID) {
        try {
            if (!this.limitList.contains(Item.func_150899_d(Integer.parseInt(itemID)))) {
                this.limitList.add(Item.func_150899_d(Integer.parseInt(itemID)));
            }
        }
        catch (Exception ex) {}
    }
    
    public void removeFromList(final String itemID) {
        try {
            this.limitList.remove(Item.func_150899_d(Integer.parseInt(itemID)));
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (this.weapon.getState()) {
            if (!this.limitList.contains(Item.func_150899_d(268))) {
                this.limitList.add(Item.func_150899_d(268));
            }
            if (!this.limitList.contains(Item.func_150899_d(272))) {
                this.limitList.add(Item.func_150899_d(272));
            }
            if (!this.limitList.contains(Item.func_150899_d(267))) {
                this.limitList.add(Item.func_150899_d(267));
            }
            if (!this.limitList.contains(Item.func_150899_d(283))) {
                this.limitList.add(Item.func_150899_d(283));
            }
            if (!this.limitList.contains(Item.func_150899_d(276))) {
                this.limitList.add(Item.func_150899_d(276));
            }
            if (!this.limitList.contains(Item.func_150899_d(271))) {
                this.limitList.add(Item.func_150899_d(271));
            }
            if (!this.limitList.contains(Item.func_150899_d(275))) {
                this.limitList.add(Item.func_150899_d(275));
            }
            if (!this.limitList.contains(Item.func_150899_d(258))) {
                this.limitList.add(Item.func_150899_d(258));
            }
            if (!this.limitList.contains(Item.func_150899_d(286))) {
                this.limitList.add(Item.func_150899_d(286));
            }
            if (!this.limitList.contains(Item.func_150899_d(279))) {
                this.limitList.add(Item.func_150899_d(279));
            }
            this.removeThings = true;
        }
        else if (this.removeThings) {
            this.limitList.remove(Item.func_150899_d(268));
            this.limitList.remove(Item.func_150899_d(272));
            this.limitList.remove(Item.func_150899_d(267));
            this.limitList.remove(Item.func_150899_d(283));
            this.limitList.remove(Item.func_150899_d(276));
            this.limitList.remove(Item.func_150899_d(271));
            this.limitList.remove(Item.func_150899_d(275));
            this.limitList.remove(Item.func_150899_d(258));
            this.limitList.remove(Item.func_150899_d(286));
            this.limitList.remove(Item.func_150899_d(279));
            this.removeThings = false;
        }
    }
    
    @Override
    public void onRenderTick(final TickEvent.RenderTickEvent e) {
        if (this.mc.field_71462_r == null && this.check(this.mc.field_71439_g)) {
            if (this.limitItems.getState() && !this.check2(this.mc.field_71439_g)) {
                return;
            }
            Mouse.poll();
            if (Mouse.isButtonDown(0)) {
                if (!this.breakingBlock((EntityPlayer)this.mc.field_71439_g) && this.breakOption.getState()) {
                    return;
                }
                if (this.jitter.getState() && MathUtil.random.nextDouble() > 0.65) {
                    final float jitterStrength = (float)this.jitterstren.getValue() * 0.5f;
                    final EntityPlayerSP thePlayer = this.mc.field_71439_g;
                    float rotationYaw;
                    if (MathUtil.random.nextBoolean()) {
                        final EntityPlayerSP thePlayer2 = this.mc.field_71439_g;
                        rotationYaw = (thePlayer2.field_70177_z += MathUtil.random.nextFloat() * jitterStrength);
                    }
                    else {
                        final EntityPlayerSP thePlayer3 = this.mc.field_71439_g;
                        rotationYaw = (thePlayer3.field_70177_z -= MathUtil.random.nextFloat() * jitterStrength);
                    }
                    thePlayer.field_70177_z = rotationYaw;
                    this.mc.field_71439_g.field_70125_A = (MathUtil.random.nextBoolean() ? ((float)(this.mc.field_71439_g.field_70125_A + MathUtil.random.nextFloat() * (jitterStrength * 0.75))) : ((float)(this.mc.field_71439_g.field_70125_A - MathUtil.random.nextFloat() * (jitterStrength * 0.75))));
                }
                if (this.nextLeftDown > 0L && this.nextLeftUp > 0L) {
                    if (System.currentTimeMillis() > this.nextLeftDown) {
                        final int attackKeyBind = this.mc.field_71474_y.field_74312_F.func_151463_i();
                        KeyBinding.func_74510_a(attackKeyBind, true);
                        KeyBinding.func_74507_a(attackKeyBind);
                        MouseUtil.sendClick(0, true);
                        if (Mouse.isButtonDown(1) && this.blockHit.getState()) {
                            this.doBlockHit = true;
                            final int useKeyBind = this.mc.field_71474_y.field_74313_G.func_151463_i();
                            KeyBinding.func_74510_a(useKeyBind, true);
                            KeyBinding.func_74507_a(useKeyBind);
                            MouseUtil.sendClick(1, true);
                        }
                        else {
                            this.doBlockHit = false;
                        }
                        this.generateLeftDelay();
                    }
                    else if (System.currentTimeMillis() > this.nextLeftUp) {
                        final int attackKeyBind = this.mc.field_71474_y.field_74312_F.func_151463_i();
                        KeyBinding.func_74510_a(attackKeyBind, false);
                        MouseUtil.sendClick(0, false);
                        if (this.doBlockHit) {
                            final int useKeyBind = this.mc.field_71474_y.field_74313_G.func_151463_i();
                            KeyBinding.func_74510_a(useKeyBind, false);
                            MouseUtil.sendClick(1, false);
                        }
                    }
                }
                else {
                    this.generateLeftDelay();
                }
            }
            else {
                this.nextLeftUp = 0L;
                this.nextLeftDown = 0L;
            }
        }
        else if (this.mc.field_71462_r instanceof GuiInventory || this.mc.field_71462_r instanceof GuiChest) {
            if (Mouse.isButtonDown(0) && (Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42))) {
                final boolean inventoryFill = this.inv.getState();
                if (!inventoryFill) {
                    return;
                }
                if (this.nextLeftUp == 0L || this.nextLeftDown == 0L) {
                    this.generateLeftDelay();
                    return;
                }
                if (System.currentTimeMillis() > this.nextLeftDown) {
                    this.generateLeftDelay();
                    this.clickInventory(this.mc.field_71462_r);
                }
            }
            else {
                this.nextLeftUp = 0L;
                this.nextLeftDown = 0L;
            }
        }
    }
    
    private void generateLeftDelay() {
        final double minCPS = this.mincps.getValue();
        final double maxCPS;
        if (minCPS > (maxCPS = this.maxcps.getValue())) {
            return;
        }
        final double CPS = minCPS + MathUtil.random.nextDouble() * (maxCPS - minCPS);
        long delay = (int)Math.round(1000.0 / CPS);
        if (System.currentTimeMillis() > this.nextDrop) {
            if (!this.dropping && MathUtil.random.nextInt(100) >= 85) {
                this.dropping = true;
                this.dropRate = 1.1 + MathUtil.random.nextDouble() * 0.15;
            }
            else {
                this.dropping = false;
            }
            this.nextDrop = System.currentTimeMillis() + 500L + MathUtil.random.nextInt(1500);
        }
        if (this.dropping) {
            delay *= (long)this.dropRate;
        }
        if (System.currentTimeMillis() > this.nextExhaust) {
            if (MathUtil.random.nextInt(100) >= 80) {
                delay += 50L + MathUtil.random.nextInt(150);
            }
            this.nextExhaust = System.currentTimeMillis() + 500L + MathUtil.random.nextInt(1500);
        }
        this.nextLeftDown = System.currentTimeMillis() + delay;
        this.nextLeftUp = System.currentTimeMillis() + delay / 2L - MathUtil.random.nextInt(10);
    }
    
    private void clickInventory(final GuiScreen screen) {
        final int var1 = Mouse.getX() * screen.field_146294_l / this.mc.field_71443_c;
        final int var2 = screen.field_146295_m - Mouse.getY() * screen.field_146295_m / this.mc.field_71440_d - 1;
        final int var3 = 0;
        try {
            this.guiScreenMethod.setAccessible(true);
            this.guiScreenMethod.invoke(screen, var1, var2, var3);
            this.guiScreenMethod.setAccessible(false);
        }
        catch (Exception ex) {}
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
