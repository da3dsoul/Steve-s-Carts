package vswe.stevescarts;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import vswe.stevescarts.Blocks.BlockCartAssembler;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Containers.ContainerBase;
import vswe.stevescarts.Containers.ContainerMinecart;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.TileEntities.TileEntityBase;

public class PacketHandler
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientPacket(ClientCustomPacketEvent event)
    {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
        byte idForCrash = -1;

        try
        {
            byte[] ex = event.packet.payload().array();
            ByteArrayDataInput reader = ByteStreams.newDataInput(ex);
            byte id = reader.readByte();
            int entityid;
            int len;
            int world;

            if (id == -1)
            {
                entityid = reader.readInt();
                len = reader.readInt();
                int data = reader.readInt();
                world = ex.length - 13;
                byte[] cart = new byte[world];

                for (int world1 = 0; world1 < world; ++world1)
                {
                    cart[world1] = reader.readByte();
                }

                World var17 = player.worldObj;
                ((BlockCartAssembler)ModBlocks.CART_ASSEMBLER.getBlock()).updateMultiBlock(var17, entityid, len, data);
            }
            else
            {
                entityid = reader.readInt();
                len = ex.length - 5;
                byte[] var14 = new byte[len];

                for (world = 0; world < len; ++world)
                {
                    var14[world] = reader.readByte();
                }

                World var15 = player.worldObj;
                MinecartModular var16 = this.getCart(entityid, var15);

                if (var16 != null)
                {
                    this.receivePacketAtCart(var16, id, var14, player);
                }
            }
        }
        catch (Exception var13)
        {
            System.out.println("The client failed to process a packet with " + (idForCrash == -1 ? "unknown id" : "id " + idForCrash));
        }
    }

    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event)
    {
        EntityPlayerMP player = ((NetHandlerPlayServer)event.handler).playerEntity;
        byte idForCrash = -1;

        try
        {
            byte[] ex = event.packet.payload().array();
            ByteArrayDataInput reader = ByteStreams.newDataInput(ex);
            byte id = reader.readByte();
            World world = player.worldObj;
            int len;

            if (player.openContainer instanceof ContainerPlayer)
            {
                len = reader.readInt();
                int data = ex.length - 5;
                byte[] con = new byte[data];

                for (int conBase = 0; conBase < data; ++conBase)
                {
                    con[conBase] = reader.readByte();
                }

                MinecartModular var17 = this.getCart(len, world);

                if (var17 != null)
                {
                    this.receivePacketAtCart(var17, id, con, player);
                }
            }
            else
            {
                len = ex.length - 1;
                byte[] var14 = new byte[len];

                for (int var15 = 0; var15 < len; ++var15)
                {
                    var14[var15] = reader.readByte();
                }

                Container var16 = player.openContainer;

                if (var16 instanceof ContainerMinecart)
                {
                    ContainerMinecart var18 = (ContainerMinecart)var16;
                    MinecartModular base = var18.cart;
                    this.receivePacketAtCart(base, id, var14, player);
                }
                else if (var16 instanceof ContainerBase)
                {
                    ContainerBase var19 = (ContainerBase)var16;
                    TileEntityBase var20 = var19.getTileEntity();

                    if (var20 != null)
                    {
                        var20.receivePacket(id, var14, player);
                    }
                }
            }
        }
        catch (Exception var13)
        {
            System.out.println("The server failed to process a packet with " + (idForCrash == -1 ? "unknown id" : "id " + idForCrash));
        }
    }

    private void receivePacketAtCart(MinecartModular cart, int id, byte[] data, EntityPlayer player)
    {
        Iterator i$ = cart.getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (id >= module.getPacketStart() && id < module.getPacketStart() + module.totalNumberOfPackets())
            {
                module.delegateReceivedPacket(id - module.getPacketStart(), data, player);
                break;
            }
        }
    }

    private MinecartModular getCart(int ID, World world)
    {
        Iterator i$ = world.loadedEntityList.iterator();
        Object e;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            e = i$.next();
        }
        while (!(e instanceof Entity) || ((Entity)e).getEntityId() != ID || !(e instanceof MinecartModular));

        return (MinecartModular)e;
    }

    public static void sendPacket(int id, byte[] extraData)
    {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(bs);

        try
        {
            ds.writeByte((byte)id);
            byte[] e = extraData;
            int len$ = extraData.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                byte b = e[i$];
                ds.writeByte(b);
            }
        }
        catch (IOException var8)
        {
            ;
        }

        StevesCarts.packetHandler.sendToServer(createPacket(bs.toByteArray()));
    }

    private static FMLProxyPacket createPacket(byte[] bytes)
    {
        ByteBuf buf = Unpooled.copiedBuffer(bytes);
        return new FMLProxyPacket(buf, "SC2");
    }

    public static void sendPacket(MinecartModular cart, int id, byte[] extraData)
    {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(bs);

        try
        {
            ds.writeByte((byte)id);
            ds.writeInt(cart.getEntityId());
            byte[] e = extraData;
            int len$ = extraData.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                byte b = e[i$];
                ds.writeByte(b);
            }
        }
        catch (IOException var9)
        {
            ;
        }

        StevesCarts.packetHandler.sendToServer(createPacket(bs.toByteArray()));
    }

    public static void sendPacketToPlayer(int id, byte[] data, EntityPlayer player, MinecartModular cart)
    {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(bs);

        try
        {
            ds.writeByte((byte)id);
            ds.writeInt(cart.getEntityId());
            byte[] e = data;
            int len$ = data.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                byte b = e[i$];
                ds.writeByte(b);
            }
        }
        catch (IOException var10)
        {
            ;
        }

        StevesCarts.packetHandler.sendTo(createPacket(bs.toByteArray()), (EntityPlayerMP)player);
    }

    public static void sendBlockInfoToClients(World world, byte[] data, int x, int y, int z)
    {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(bs);

        try
        {
            ds.writeByte(-1);
            ds.writeInt(x);
            ds.writeInt(y);
            ds.writeInt(z);
            byte[] e = data;
            int len$ = data.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                byte b = e[i$];
                ds.writeByte(b);
            }
        }
        catch (IOException var11)
        {
            ;
        }

        StevesCarts.packetHandler.sendToAllAround(createPacket(bs.toByteArray()), new TargetPoint(world.provider.dimensionId, (double)x, (double)y, (double)z, 64.0D));
    }
}
