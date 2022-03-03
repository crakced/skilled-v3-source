 package me.vene.skilled.modules.mods.combat;
 
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.MathUtil;
 import me.vene.skilled.utilities.StringRegistry;
 import me.vene.skilled.values.BooleanValue;
 import me.vene.skilled.values.NumberValue;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.entity.EntityPlayerSP;
 import net.minecraft.item.ItemAxe;
 import net.minecraft.item.ItemSword;
 import net.minecraft.potion.Potion;

 public class Velocity extends Module
 {
	 
  private String hor;
  private String ver;
  private String chanceString;
  private NumberValue HorizontalModifier;
  private NumberValue VerticalModifier;
  private NumberValue chanceValue;
  private BooleanValue onlyTargetting;
  private static BooleanValue onlyClick;
  private static BooleanValue Weapon;
  private static BooleanValue Sprinting;
  private static BooleanValue OnlyGround;
  private static Minecraft mc;
  boolean lpx;
 
 
 public Velocity() {
     super(StringRegistry.register(new String(new char[] { 'V', 'e', 'l', 'o', 'c', 'i', 't', 'y' })), 0, Category.C);
     this.hor = StringRegistry.register(new String(new char[] { 'H', 'o', 'r', 'i', 'z', 'o', 'n', 't', 'a', 'l' }));
     this.ver = StringRegistry.register(new String(new char[] { 'V', 'e', 'r', 't', 'i', 'c', 'a', 'l' }));
     this.chanceString = StringRegistry.register(new String(new char[] { 'C', 'h', 'a', 'n', 'c', 'e' }));
     this.HorizontalModifier = new NumberValue(StringRegistry.register(this.hor), 92.0, 0.0, 100.0);
     this.VerticalModifier = new NumberValue(StringRegistry.register(this.ver), 100.0, 0.0, 100.0);
     this.chanceValue = new NumberValue(StringRegistry.register(this.chanceString), 100.0, 0.0, 100.0);
     this.onlyTargetting = new BooleanValue(StringRegistry.register("Only Targetting"), true);
     Velocity.Sprinting = new BooleanValue(StringRegistry.register("Sprinting"), false);
     Velocity.onlyClick = new BooleanValue(StringRegistry.register("Only Click"), false);
     Velocity.OnlyGround = new BooleanValue(StringRegistry.register("Only Ground"), false);
     Velocity.Weapon = new BooleanValue(StringRegistry.register("Weapon"), false);
     this.mc = Minecraft.func_71410_x();
     Velocity.mc = Minecraft.func_71410_x();
     this.lpx = false;
     this.addValue(this.HorizontalModifier);
     this.addValue(this.VerticalModifier);
     this.addValue(this.chanceValue);
     this.addOption(this.onlyTargetting);
     this.addOption(Velocity.Sprinting);
     this.addOption(Velocity.onlyClick);
     this.addOption(Velocity.OnlyGround);
     this.addOption(Velocity.Weapon);
   }

   public void onLivingUpdate(EntityPlayerSP player) {
     if ((Minecraft.func_71410_x()).field_71439_g == null) {
       return;
     }
     if ((Minecraft.func_71410_x()).field_71441_e == null) {
       return;
     }
  if (!Velocity.mc.field_71439_g.func_70090_H() == false) { 
	    return;	 
        }
   if (!Velocity.mc.field_71439_g.func_180799_ab() == false) {
	    return;	 
    }  
   if (!Velocity.mc.field_71439_g.func_70617_f_() == false) {
    return;	 
   }

  if (!Velocity.mc.field_71439_g.func_70093_af() == false) {
    return;	 
 }

  if (!(Velocity.mc.field_71439_g.func_71024_bL().func_75116_a() > 6)) {
       return;	 
  }

   if (!Velocity.mc.field_71439_g.func_70644_a(Potion.field_76436_u) == false) {
        return;	 
  } 

   if (!Velocity.mc.field_71439_g.func_70644_a(Potion.field_76421_d) == false) {
         return;	 
  }
   if (!Velocity.mc.field_71439_g.func_70644_a(Potion.field_76419_f) == false) {
  return;	 
  }

     if (!Velocity.mc.field_71439_g.func_70644_a(Potion.field_82731_v) == false) {
   return;	 
    }

     if (!Velocity.mc.field_71439_g.func_70644_a(Potion.field_76430_j) == false) {
    return;	 
   }
     if (!Velocity.mc.field_71439_g.func_70644_a(Potion.field_76440_q) == false) {
     return;	 
   }
     
 	 if (!Velocity.mc.field_71474_y.field_74311_E.func_151470_d() == false) {
           return;
     }  
    
 if (Velocity.onlyClick.getState() && !Velocity.mc.field_71474_y.field_74312_F.func_151470_d()) {
   this.lpx = false;
   return;
       }
 
  if (Velocity.OnlyGround.getState()) {
      if (!Velocity.mc.field_71439_g.field_70122_E) { 
     return;   
     }
  }
if (Velocity.Sprinting.getState()) {
     if (!Velocity.mc.field_71439_g.func_70051_ag()) {
     return;   
       }
   }
if (Velocity.Weapon.getState()) {
  if (Velocity.mc.field_71439_g.func_71045_bC() == null) {
      return;
      }
  if (!(Velocity.mc.field_71439_g.func_71045_bC().func_77973_b() instanceof ItemSword) && !(Velocity.mc.field_71439_g.func_71045_bC().func_77973_b() instanceof ItemAxe)) {
      return;
      }
  }

if (this.onlyTargetting.getState() && (this.mc.field_71476_x == null || this.mc.field_71476_x.field_72308_g == null)) {
    return;
  }

 if (MathUtil.random.nextInt(100) >= (int)this.chanceValue.getValue()) {
       return;
     }
     double vertmodifier = this.VerticalModifier.getValue() / 100.0D;
     double horzmodifier = this.HorizontalModifier.getValue() / 100.0D;
     if ((Minecraft.func_71410_x()).field_71439_g.field_70737_aN == (Minecraft.func_71410_x()).field_71439_g.field_70738_aO && (Minecraft.func_71410_x()).field_71439_g.field_70738_aO > 0) {
       this.mc.field_71439_g.field_70159_w *= horzmodifier;
       this.mc.field_71439_g.field_70179_y *= horzmodifier;
       this.mc.field_71439_g.field_70181_x *= vertmodifier;
          } 
        }
      } 