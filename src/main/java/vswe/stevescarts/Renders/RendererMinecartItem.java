package vswe.stevescarts.Renders;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.Modules.ModuleBase;

public class RendererMinecartItem implements IItemRenderer
{
    public RendererMinecartItem()
    {
        MinecraftForgeClient.registerItemRenderer(ModItems.carts, this);
    }

    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    public void renderItem(ItemRenderType type, ItemStack item, Object ... data)
    {
        GL11.glPushMatrix();
        GL11.glScalef(-1.0F, -1.0F, 1.0F);

        if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glTranslatef(0.0F, -1.0F, 1.0F);
        }
        else if (type == ItemRenderType.INVENTORY)
        {
            GL11.glTranslatef(0.0F, 0.1F, 0.0F);
        }

        NBTTagCompound info = item.getTagCompound();

        if (info != null)
        {
            NBTTagByteArray moduleIDTag = (NBTTagByteArray)info.getTag("Modules");
            byte[] bytes = moduleIDTag.func_150292_c();
            HashMap models = new HashMap();
            float lowestMult = 1.0F;
            byte[] i$ = bytes;
            int model = bytes.length;
            int i$1;
            byte id;
            ModuleData module;

            for (i$1 = 0; i$1 < model; ++i$1)
            {
                id = i$[i$1];
                module = (ModuleData)ModuleData.getList().get(Byte.valueOf(id));

                if (module != null && module.haveModels(true))
                {
                    if (module.getModelMult() < lowestMult)
                    {
                        lowestMult = module.getModelMult();
                    }

                    models.putAll(module.getModels(true));
                }
            }

            i$ = bytes;
            model = bytes.length;

            for (i$1 = 0; i$1 < model; ++i$1)
            {
                id = i$[i$1];
                module = (ModuleData)ModuleData.getList().get(Byte.valueOf(id));

                if (module != null && module.haveRemovedModels())
                {
                    Iterator i$2 = module.getRemovedModels().iterator();

                    while (i$2.hasNext())
                    {
                        String str = (String)i$2.next();
                        models.remove(str);
                    }
                }
            }

            if (type == ItemRenderType.INVENTORY)
            {
                GL11.glScalef(lowestMult, lowestMult, lowestMult);
            }

            Iterator var16 = models.values().iterator();

            while (var16.hasNext())
            {
                ModelCartbase var17 = (ModelCartbase)var16.next();
                var17.render((Render)null, (ModuleBase)null, 0.0F, 0.0F, 0.0F, 0.0625F, 0.0F);
            }
        }

        GL11.glPopMatrix();
    }
}
