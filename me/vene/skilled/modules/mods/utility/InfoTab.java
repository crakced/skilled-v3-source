 package me.vene.skilled.modules.mods.utility;
 
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumChatFormatting;
 import java.awt.Color;
 import java.lang.reflect.Field;
 import java.nio.FloatBuffer;
 import java.util.ArrayList;
 import me.vene.skilled.gui.GUI;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.StringRegistry;
 import me.vene.skilled.values.BooleanValue;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.client.renderer.ActiveRenderInfo;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.entity.RenderManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.projectile.EntityArrow;
 import net.minecraft.entity.projectile.EntityEgg;
 import net.minecraft.entity.projectile.EntityFireball;
 import net.minecraft.entity.projectile.EntitySnowball;
 import net.minecraft.util.EnumChatFormatting;
 import net.minecraftforge.client.event.RenderGameOverlayEvent;
 import org.lwjgl.opengl.GL11;
 
public class InfoTab extends Module
{
    private Minecraft mc;
    private BooleanValue tracerBool;
    private BooleanValue fireballsBool;
    private BooleanValue arrowsBool;
    private BooleanValue snowballsBool;
    private BooleanValue eggsBool;
    private Field arrowInGroundField;
    private Field modelViewField;
    
    public InfoTab() {
        super("Warnings", 0, Category.U);
        this.mc = Minecraft.func_71410_x();
        this.tracerBool = new BooleanValue("Tracers", true);
        this.fireballsBool = new BooleanValue("Fireballs", true);
        this.arrowsBool = new BooleanValue("Arrows", true);
        this.snowballsBool = new BooleanValue("Snowballs", false);
        this.eggsBool = new BooleanValue("Eggs", false);
        this.addOption(this.fireballsBool);
        this.addOption(this.arrowsBool);
        this.addOption(this.snowballsBool);
        this.addOption(this.eggsBool);
        try {
            this.arrowInGroundField = EntityArrow.class.getDeclaredField(new String(new char[] { 'f', 'i', 'e', 'l', 'd', '_', '7', '0', '1', '9', '3', '_', 'a' }));
        }
        catch (Exception e) {
            try {
                this.arrowInGroundField = EntityArrow.class.getDeclaredField(new String(new char[] { 'i', 'n', 'G', 'r', 'o', 'u', 'n', 'd' }));
            }
            catch (Exception ex) {}
        }
        if (this.arrowInGroundField != null) {
            this.arrowInGroundField.setAccessible(true);
        }
    }
    
    private float[] getViewProjection() throws IllegalAccessException {
        final FloatBuffer a = (FloatBuffer)this.modelViewField.get(ActiveRenderInfo.class);
        final float[] modelView = new float[16];
        for (int i = 0; i < 16; ++i) {
            modelView[i] = a.get(i);
            System.out.println("a " + modelView[i]);
        }
        return modelView;
    }
    
    @Override
    public void onRenderText(final RenderGameOverlayEvent.Post e) {
        if (this.mc.field_71441_e == null) {
            return;
        }
        try {
            if (GUI.renderInfo) {
                final ArrayList<String> alertArray = new ArrayList<String>();
                for (final Object object : this.mc.field_71441_e.field_72996_f) {
                    if (object instanceof EntityFireball && this.fireballsBool.getState()) {
                        alertArray.add(EnumChatFormatting.RED + "FIREBALL ALERT: " + EnumChatFormatting.GOLD + String.valueOf((int)((EntityFireball)object).func_70032_d((Entity)Minecraft.func_71410_x().field_71439_g)));
                    }
                    if (object instanceof EntitySnowball && this.snowballsBool.getState()) {
                        alertArray.add(EnumChatFormatting.DARK_AQUA + "SNOWBALL ALERT: " + EnumChatFormatting.GOLD + String.valueOf((int)((EntitySnowball)object).func_70032_d((Entity)Minecraft.func_71410_x().field_71439_g)));
                    }
                    if (object instanceof EntityEgg && this.eggsBool.getState()) {
                        alertArray.add(EnumChatFormatting.DARK_RED + "EGG ALERT: " + EnumChatFormatting.GOLD + String.valueOf((int)((EntityEgg)object).func_70032_d((Entity)Minecraft.func_71410_x().field_71439_g)));
                    }
                    try {
                        if (!(object instanceof EntityArrow) || !this.arrowsBool.getState() || this.arrowInGroundField.getBoolean(object)) {
                            continue;
                        }
                        alertArray.add(EnumChatFormatting.DARK_PURPLE + "ARROW ALERT: " + EnumChatFormatting.GOLD + String.valueOf((int)((EntityArrow)object).func_70032_d((Entity)Minecraft.func_71410_x().field_71439_g)));
                    }
                    catch (IllegalAccessException ex) {}
                }
                for (int i = 0; i < alertArray.size(); ++i) {
                    Minecraft.func_71410_x().field_71466_p.func_78276_b((String)alertArray.get(i), GUI.infoXPos + 2, GUI.infoYPos + 15 + i * 9, -1);
                }
                Entity p = null;
                Color currentColor = null;
                if (this.tracerBool.getState()) {
                    for (final Object object2 : this.mc.field_71441_e.field_72996_f) {
                        p = null;
                        if (object2 instanceof EntityFireball && this.fireballsBool.getState()) {
                            p = (Entity)object2;
                            currentColor = Color.ORANGE;
                        }
                        if (object2 instanceof EntitySnowball && this.snowballsBool.getState()) {
                            p = (Entity)object2;
                            currentColor = Color.cyan;
                        }
                        if (object2 instanceof EntityEgg && this.eggsBool.getState()) {
                            p = (Entity)object2;
                            currentColor = Color.RED;
                        }
                        try {
                            if (object2 instanceof EntityArrow && this.arrowsBool.getState() && !this.arrowInGroundField.getBoolean(object2)) {
                                p = (Entity)object2;
                                currentColor = Color.MAGENTA;
                            }
                        }
                        catch (IllegalAccessException ex2) {}
                        if (p != null) {
                            final EntityPlayerSP player = this.mc.field_71439_g;
                            GlStateManager.func_179147_l();
                            GlStateManager.func_179097_i();
                            GlStateManager.func_179090_x();
                            GlStateManager.func_179140_f();
                            GL11.glEnable(2848);
                            GlStateManager.func_179112_b(770, 771);
                            GL11.glLineWidth(1.0f);
                            final String Xstring = new String(new char[] { 'f', 'i', 'e', 'l', 'd', '_', '7', '8', '7', '2', '5', '_', 'b' });
                            final String Ystring = new String(new char[] { 'f', 'i', 'e', 'l', 'd', '_', '7', '8', '7', '2', '6', '_', 'c' });
                            final String Zstring = new String(new char[] { 'f', 'i', 'e', 'l', 'd', '_', '7', '8', '7', '2', '3', '_', 'd' });
                            final double renderPosX = GetFieldByReflection(RenderManager.class, this.mc.func_175598_ae(), StringRegistry.register(Xstring));
                            final double renderPosY = GetFieldByReflection(RenderManager.class, this.mc.func_175598_ae(), StringRegistry.register(Ystring));
                            final double renderPosZ = GetFieldByReflection(RenderManager.class, this.mc.func_175598_ae(), StringRegistry.register(Zstring));
                            final double x = p.field_70142_S + (p.field_70165_t - p.field_70142_S) * e.partialTicks - renderPosX;
                            final double y = p.field_70137_T + (p.field_70163_u - p.field_70137_T) * e.partialTicks - renderPosY + 1.0;
                            final double z = p.field_70136_U + (p.field_70161_v - p.field_70136_U) * e.partialTicks - renderPosZ;
                            GlStateManager.func_179131_c((float)currentColor.getRed(), (float)currentColor.getGreen(), (float)currentColor.getBlue(), 1.0f);
                            GL11.glBegin(1);
                            GL11.glVertex3d(0.0, (double)player.func_70047_e(), 0.0);
                            GL11.glVertex3d(x, y, z);
                            GL11.glEnd();
                        }
                    }
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179145_e();
                    GL11.glDisable(2848);
                    GL11.glLineWidth(1.0f);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
        catch (Exception ex3) {}
    }
    
    public static <T, E> T GetFieldByReflection(final Class<? super E> classToAccess, final E instance, final String... fieldNames) {
        Field field = null;
        for (final String fieldName : fieldNames) {
            try {
                field = classToAccess.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException ex) {}
            if (field != null) {
                break;
            }
        }
        if (field != null) {
            field.setAccessible(true);
            T fieldT = null;
            try {
                fieldT = (T)field.get(instance);
            }
            catch (IllegalArgumentException ex2) {}
            catch (IllegalAccessException ex3) {}
            return fieldT;
        }
        return null;
    }
}
