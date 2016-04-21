package vswe.stevescarts.Helpers;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Items.ItemCarts;

public abstract class CartVersion
{
    private static ArrayList<CartVersion> versions = new ArrayList();

    public CartVersion()
    {
        versions.add(this);
    }

    public abstract void update(ArrayList<Byte> var1);

    public static byte[] updateCart(MinecartModular cart, byte[] data)
    {
        if (cart.cartVersion != getCurrentVersion())
        {
            data = updateArray(data, cart.cartVersion);
            cart.cartVersion = (byte)getCurrentVersion();
        }

        return data;
    }

    private static byte[] updateArray(byte[] data, int version)
    {
        ArrayList modules = new ArrayList();
        byte[] i = data;
        int len$ = data.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            byte b = i[i$];
            modules.add(Byte.valueOf(b));
        }

        while (version < getCurrentVersion())
        {
            ((CartVersion)versions.get(version++)).update(modules);
        }

        data = new byte[modules.size()];

        for (int var7 = 0; var7 < data.length; ++var7)
        {
            data[var7] = ((Byte)modules.get(var7)).byteValue();
        }

        return data;
    }

    public static void updateItemStack(ItemStack item)
    {
        if (item != null && item.getItem() instanceof ItemCarts)
        {
            NBTTagCompound info = item.getTagCompound();

            if (info != null)
            {
                byte version = info.getByte("CartVersion");

                if (version != getCurrentVersion())
                {
                    info.setByteArray("Modules", updateArray(info.getByteArray("Modules"), version));
                    addVersion(info);
                }
            }
        }
    }

    public static void addVersion(ItemStack item)
    {
        if (item != null && item.getItem() instanceof ItemCarts)
        {
            NBTTagCompound info = item.getTagCompound();

            if (info != null)
            {
                addVersion(info);
            }
        }
    }

    private static void addVersion(NBTTagCompound info)
    {
        info.setByte("CartVersion", (byte)getCurrentVersion());
    }

    private static int getCurrentVersion()
    {
        return versions.size();
    }

    static
    {
        new CartVersion()
        {
            public void update(ArrayList<Byte> modules)
            {
                int index = modules.indexOf(Byte.valueOf((byte)17));

                if (index != -1)
                {
                    modules.set(index, Byte.valueOf((byte)16));
                }

                if (modules.contains(Byte.valueOf((byte)16)))
                {
                    modules.add(Byte.valueOf((byte)64));
                }
            }
        };
        new CartVersion()
        {
            public void update(ArrayList<Byte> modules) {}
        };
    }
}
