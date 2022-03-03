 package me.vene.skilled.modules.mods.combat;
 
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
 import me.vene.skilled.modules.Category;
 import me.vene.skilled.modules.Module;
 import me.vene.skilled.utilities.ClientUtil;
 import me.vene.skilled.utilities.StringRegistry;
 import me.vene.skilled.values.NumberValue;
 import net.minecraft.client.Minecraft;
 import net.minecraft.util.MovingObjectPosition;

public class HitBoxes extends Module
{
    private Minecraft mc;
    private String expandstring;
    private NumberValue expandVal;
    
    public HitBoxes() {
        super(StringRegistry.register(new String(new char[] { 'H', 'i', 't', 'B', 'o', 'x', 'e', 's' })), 0, Category.C);
        this.mc = Minecraft.func_71410_x();
        this.expandstring = new String(new char[] { 'E', 'x', 'p', 'a', 'n', 'd' });
        this.addValue(this.expandVal = new NumberValue(StringRegistry.register(this.expandstring), 0.0, 0.0, 1.0));
    }
    
    @Override
    public void onMouseOver(final float memes) {
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || this.mc.field_71462_r != null) {
            return;
        }
        if (this.mc.field_71439_g.func_70115_ae()) {
            return;
        }
        if (this.mc.field_71439_g.field_70128_L) {
            return;
        }
        final double expand = this.expandVal.getValue();
        final MovingObjectPosition object = ClientUtil.getMouseOver(3.0, expand, memes);
        if (object != null) {
            this.mc.field_71476_x = object;
        }
    }
}
