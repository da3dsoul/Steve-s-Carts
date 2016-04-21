package vswe.stevescarts.Modules.Realtimers;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.EnchantmentInfo;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleEnchants;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleProjectile;
import vswe.stevescarts.Slots.SlotArrow;
import vswe.stevescarts.Slots.SlotBase;

public class ModuleShooter extends ModuleBase implements ISuppliesModule
{
    private ArrayList<ModuleProjectile> projectiles;
    private ModuleEnchants enchanter;
    private int pipeSelectionX;
    private int pipeSelectionY;
    private int intervalSelectionX;
    private int intervalSelectionY;
    private int[] intervalSelection;
    private int[] intervalDragArea;
    private int currentCooldownState;
    private int dragState = -1;
    private final ArrayList<Integer> pipes = new ArrayList();
    private final float[] pipeRotations;
    private final int[] AInterval = new int[] {1, 3, 5, 7, 10, 13, 17, 21, 27, 35, 44, 55, 70, 95, 130, 175, 220, 275, 340, 420, 520, 650};
    private int arrowTick;
    private int arrowInterval = 5;

    public ModuleShooter(MinecartModular cart)
    {
        super(cart);
        this.generatePipes(this.pipes);
        this.pipeRotations = new float[this.pipes.size()];
        this.generateInterfaceRegions();
    }

    public void init()
    {
        super.init();
        this.projectiles = new ArrayList();
        Iterator i$ = this.getCart().getModules().iterator();

        while (i$.hasNext())
        {
            ModuleBase module = (ModuleBase)i$.next();

            if (module instanceof ModuleProjectile)
            {
                this.projectiles.add((ModuleProjectile)module);
            }
            else if (module instanceof ModuleEnchants)
            {
                this.enchanter = (ModuleEnchants)module;
                this.enchanter.addType(EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER);
            }
        }
    }

    protected int getInventoryHeight()
    {
        return 2;
    }

    protected SlotBase getSlot(int slotId, int x, int y)
    {
        return new SlotArrow(this.getCart(), this, slotId, 8 + x * 18, 23 + y * 18);
    }

    public boolean hasGui()
    {
        return true;
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.SHOOTER.translate(new String[0]), 8, 6, 4210752);
        int delay = this.AInterval[this.arrowInterval];
        double freq = 20.0D / (double)(delay + 1);
        String s = String.valueOf((double)((int)(freq * 1000.0D)) / 1000.0D);
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.FREQUENCY.translate(new String[0]) + ":", this.intervalDragArea[0] + this.intervalDragArea[2] + 5, 15, 4210752);
        this.drawString(gui, s, this.intervalDragArea[0] + this.intervalDragArea[2] + 5, 23, 4210752);
        s = String.valueOf((double)delay / 20.0D + Localization.MODULES.ATTACHMENTS.SECONDS.translate(new String[0]));
        this.drawString(gui, Localization.MODULES.ATTACHMENTS.DELAY.translate(new String[0]) + ":", this.intervalDragArea[0] + this.intervalDragArea[2] + 5, 35, 4210752);
        this.drawString(gui, s, this.intervalDragArea[0] + this.intervalDragArea[2] + 5, 43, 4210752);
    }

    public int guiWidth()
    {
        return super.guiWidth() + this.guiExtraWidth();
    }

    protected int guiExtraWidth()
    {
        return 112;
    }

    public int guiHeight()
    {
        return Math.max(super.guiHeight(), this.guiRequiredHeight());
    }

    protected int guiRequiredHeight()
    {
        return 67;
    }

    protected void generateInterfaceRegions()
    {
        this.pipeSelectionX = this.guiWidth() - 110;
        this.pipeSelectionY = (this.guiHeight() - 12 - 26) / 2 + 12;
        this.intervalSelectionX = this.pipeSelectionX + 26 + 8;
        this.intervalSelectionY = 10;
        this.intervalSelection = new int[] {this.intervalSelectionX, this.intervalSelectionY, 14, 53};
        this.intervalDragArea = new int[] {this.intervalSelectionX - 4, this.intervalSelectionY, 40, 53};
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/shooter.png");
        this.drawImage(gui, this.pipeSelectionX + 9, this.pipeSelectionY + 9 - 1, 0, 104, 8, 9);
        int size;
        int targetX;
        int targetY;
        int srcX;

        for (size = 0; size < this.pipes.size(); ++size)
        {
            targetX = ((Integer)this.pipes.get(size)).intValue();
            targetY = targetX % 3;
            srcX = targetX / 3;
            boolean srcY = this.isPipeActive(size);
            boolean len = this.inRect(x, y, this.getRectForPipe(targetX)) || this.currentCooldownState == 0 && srcY;
            int srcX1 = targetY * 9;

            if (!srcY)
            {
                srcX1 += 26;
            }

            int srcY1 = srcX * 9;

            if (len)
            {
                srcY1 += 26;
            }

            this.drawImage(gui, this.getRectForPipe(targetX), srcX1, srcY1);
        }

        this.drawImage(gui, this.intervalSelection, 42, 52);
        size = (int)((float)this.arrowInterval / (float)this.AInterval.length * 4.0F);
        targetX = this.intervalSelectionX + 7;
        targetY = this.intervalSelectionY + this.arrowInterval * 2;
        byte var12 = 0;
        int var13 = 52 + size * 13;
        this.drawImage(gui, targetX, targetY, var12, var13, 25, 13);
        srcX = var12 + 25;
        targetX += 7;
        this.drawImage(gui, targetX, targetY + 1, srcX, var13 + 1, 1, 11);
        this.drawImage(gui, targetX + 1, targetY + 2, srcX + 1, var13 + 2, 1, 9);
        this.drawImage(gui, targetX + 1, targetY + 1, srcX + 1, var13 + 1, Math.min(this.currentCooldownState, 15), 2);
        this.drawImage(gui, targetX + 15, targetY + 1, srcX + 15, var13 + 1, 2, Math.max(Math.min(this.currentCooldownState, 25) - 15, 0));
        int var14 = Math.max(Math.min(this.currentCooldownState, 41) - 25, 0);
        this.drawImage(gui, targetX + 1 + (16 - var14), targetY + 10, srcX + 1 + (16 - var14), var13 + 10, var14, 2);
    }

    private int getCurrentCooldownState()
    {
        double perc = (double)this.arrowTick / (double)this.AInterval[this.arrowInterval];
        this.currentCooldownState = (int)(41.0D * perc);
        return this.currentCooldownState;
    }

    private int[] getRectForPipe(int pipe)
    {
        return new int[] {this.pipeSelectionX + pipe % 3 * 9, this.pipeSelectionY + pipe / 3 * 9, 8, 8};
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int button)
    {
        if (button == 0)
        {
            if (this.inRect(x, y, this.intervalDragArea))
            {
                this.dragState = y - (this.intervalSelectionY + this.arrowInterval * 2);
            }
            else
            {
                for (int i = 0; i < this.pipes.size(); ++i)
                {
                    if (this.inRect(x, y, this.getRectForPipe(((Integer)this.pipes.get(i)).intValue())))
                    {
                        this.sendPacket(0, (byte)i);
                        break;
                    }
                }
            }
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (button != -1)
        {
            this.dragState = -1;
        }
        else if (this.dragState != -1)
        {
            int interval = (y + this.getCart().getRealScrollY() - this.intervalSelectionY - this.dragState) / 2;

            if (interval != this.arrowInterval && interval >= 0 && interval < this.AInterval.length)
            {
                this.sendPacket(1, (byte)interval);
            }
        }
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        byte info;

        if (id == 0)
        {
            info = this.getActivePipes();
            info = (byte)(info ^ 1 << data[0]);
            this.setActivePipes(info);
        }
        else if (id == 1)
        {
            info = data[0];

            if (info < 0)
            {
                info = 0;
            }
            else if (info >= this.AInterval.length)
            {
                info = (byte)(this.AInterval.length - 1);
            }

            this.arrowInterval = info;
            this.arrowTick = this.AInterval[info];
        }
    }

    public int numberOfPackets()
    {
        return 2;
    }

    public int numberOfGuiData()
    {
        return 2;
    }

    protected void checkGuiData(Object[] info)
    {
        this.updateGuiData(info, 0, (short)this.currentCooldownState);
        this.updateGuiData(info, 1, (short)this.arrowInterval);
    }

    public void receiveGuiData(int id, short data)
    {
        if (id == 0)
        {
            this.currentCooldownState = data;
        }
        else if (id == 1)
        {
            this.arrowInterval = data;
        }
    }

    public void update()
    {
        super.update();

        if (!this.getCart().worldObj.isRemote)
        {
            if (this.arrowTick > 0)
            {
                --this.arrowTick;
            }
            else
            {
                this.Shoot();
            }
        }
        else
        {
            this.rotatePipes(false);
        }
    }

    protected void generatePipes(ArrayList<Integer> list)
    {
        for (int i = 0; i < 9; ++i)
        {
            if (i != 4)
            {
                list.add(Integer.valueOf(i));
            }
        }
    }

    protected boolean hasProjectileItem()
    {
        return this.getProjectileItem(false) != null;
    }

    protected ItemStack getProjectileItem(boolean flag)
    {
        if (flag && this.enchanter != null && this.enchanter.useInfinity())
        {
            flag = false;
        }

        for (int i = 0; i < this.getInventorySize(); ++i)
        {
            if (this.getStack(i) != null && this.isValidProjectileItem(this.getStack(i)))
            {
                ItemStack projectile = this.getStack(i).copy();
                projectile.stackSize = 1;

                if (flag && !this.getCart().hasCreativeSupplies())
                {
                    --this.getStack(i).stackSize;

                    if (this.getStack(i).stackSize == 0)
                    {
                        this.setStack(i, (ItemStack)null);
                    }
                }

                return projectile;
            }
        }

        return null;
    }

    protected void Shoot()
    {
        this.setTimeToNext(this.AInterval[this.arrowInterval]);

        if ((this.getCart().pushX == 0.0D || this.getCart().pushZ == 0.0D) && (this.getCart().pushX != 0.0D || this.getCart().pushZ != 0.0D) && this.getCart().hasFuel())
        {
            boolean hasShot = false;

            for (int i = 0; i < this.pipes.size(); ++i)
            {
                if (this.isPipeActive(i))
                {
                    int pipe = ((Integer)this.pipes.get(i)).intValue();

                    if (!this.hasProjectileItem())
                    {
                        break;
                    }

                    int x = pipe % 3 - 1;
                    int y = pipe / 3 - 1;

                    if (this.getCart().pushZ > 0.0D)
                    {
                        y *= -1;
                        x *= -1;
                    }
                    else if (this.getCart().pushZ >= 0.0D)
                    {
                        int projectile;

                        if (this.getCart().pushX < 0.0D)
                        {
                            projectile = -x;
                            x = y;
                            y = projectile;
                        }
                        else if (this.getCart().pushX > 0.0D)
                        {
                            projectile = x;
                            x = -y;
                            y = projectile;
                        }
                    }

                    Entity var7 = this.getProjectile((Entity)null, this.getProjectileItem(true));
                    var7.setPosition(this.getCart().posX + (double)x * 1.5D, this.getCart().posY + 0.75D, this.getCart().posZ + (double)y * 1.5D);
                    this.setHeading(var7, (double)x, 0.10000000149011612D, (double)y, 1.6F, 12.0F);
                    this.setProjectileDamage(var7);
                    this.setProjectileOnFire(var7);
                    this.setProjectileKnockback(var7);
                    this.getCart().worldObj.spawnEntityInWorld(var7);
                    hasShot = true;
                    this.damageEnchant();
                }
            }

            if (hasShot)
            {
                this.getCart().worldObj.playAuxSFX(1002, (int)this.getCart().posX, (int)this.getCart().posY, (int)this.getCart().posZ, 0);
            }
        }
    }

    protected void damageEnchant()
    {
        if (this.enchanter != null)
        {
            this.enchanter.damageEnchant(EnchantmentInfo.ENCHANTMENT_TYPE.SHOOTER, 1);
        }
    }

    protected void setProjectileOnFire(Entity projectile)
    {
        if (this.enchanter != null && this.enchanter.useFlame())
        {
            projectile.setFire(100);
        }
    }

    protected void setProjectileDamage(Entity projectile)
    {
        if (this.enchanter != null && projectile instanceof EntityArrow)
        {
            int power = this.enchanter.getPowerLevel();

            if (power > 0)
            {
                EntityArrow arrow = (EntityArrow)projectile;
                arrow.setDamage(arrow.getDamage() + (double)power * 0.5D + 0.5D);
            }
        }
    }

    protected void setProjectileKnockback(Entity projectile)
    {
        if (this.enchanter != null && projectile instanceof EntityArrow)
        {
            int punch = this.enchanter.getPunchLevel();

            if (punch > 0)
            {
                EntityArrow arrow = (EntityArrow)projectile;
                arrow.setKnockbackStrength(punch);
            }
        }
    }

    protected void setHeading(Entity projectile, double motionX, double motionY, double motionZ, float motionMult, float motionNoise)
    {
        if (projectile instanceof IProjectile)
        {
            ((IProjectile)projectile).setThrowableHeading(motionX, motionY, motionZ, motionMult, motionNoise);
        }
        else if (projectile instanceof EntityFireball)
        {
            EntityFireball fireball = (EntityFireball)projectile;
            double totalMotion = (double)MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
            fireball.accelerationX = motionX / totalMotion * 0.1D;
            fireball.accelerationY = motionY / totalMotion * 0.1D;
            fireball.accelerationZ = motionZ / totalMotion * 0.1D;
        }
    }

    protected Entity getProjectile(Entity target, ItemStack item)
    {
        Iterator i$ = this.projectiles.iterator();
        ModuleProjectile module;

        do
        {
            if (!i$.hasNext())
            {
                return new EntityArrow(this.getCart().worldObj);
            }

            module = (ModuleProjectile)i$.next();
        }
        while (!module.isValidProjectile(item));

        return module.createProjectile(target, item);
    }

    public boolean isValidProjectileItem(ItemStack item)
    {
        Iterator i$ = this.projectiles.iterator();
        ModuleProjectile module;

        do
        {
            if (!i$.hasNext())
            {
                return item.getItem() == Items.arrow;
            }

            module = (ModuleProjectile)i$.next();
        }
        while (!module.isValidProjectile(item));

        return true;
    }

    protected void setTimeToNext(int val)
    {
        this.arrowTick = val;
    }

    private void rotatePipes(boolean isNew)
    {
        float minRotation = 0.0F;
        float maxRotation = ((float)Math.PI / 4F);
        float speed = 0.15F;

        for (int i = 0; i < this.pipes.size(); ++i)
        {
            boolean isActive = this.isPipeActive(i);

            if (isNew && isActive)
            {
                this.pipeRotations[i] = minRotation;
            }
            else if (isNew && !isActive)
            {
                this.pipeRotations[i] = maxRotation;
            }
            else if (isActive && this.pipeRotations[i] > minRotation)
            {
                this.pipeRotations[i] -= speed;

                if (this.pipeRotations[i] < minRotation)
                {
                    this.pipeRotations[i] = minRotation;
                }
            }
            else if (!isActive && this.pipeRotations[i] < maxRotation)
            {
                this.pipeRotations[i] += speed;

                if (this.pipeRotations[i] > maxRotation)
                {
                    this.pipeRotations[i] = maxRotation;
                }
            }
        }
    }

    public int numberOfDataWatchers()
    {
        return 1;
    }

    public void initDw()
    {
        this.addDw(0, 0);
    }

    public void setActivePipes(byte val)
    {
        this.updateDw(0, val);
    }

    public byte getActivePipes()
    {
        return this.isPlaceholder() ? this.getSimInfo().getActivePipes() : this.getDw(0);
    }

    protected boolean isPipeActive(int id)
    {
        return (this.getActivePipes() & 1 << id) != 0;
    }

    public int getPipeCount()
    {
        return this.pipes.size();
    }

    public float getPipeRotation(int id)
    {
        return this.pipeRotations[id];
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("Pipes", id), this.getActivePipes());
        tagCompound.setByte(this.generateNBTName("Interval", id), (byte)this.arrowInterval);
        this.saveTick(tagCompound, id);
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        this.setActivePipes(tagCompound.getByte(this.generateNBTName("Pipes", id)));
        this.arrowInterval = tagCompound.getByte(this.generateNBTName("Interval", id));
        this.loadTick(tagCompound, id);
    }

    protected void saveTick(NBTTagCompound tagCompound, int id)
    {
        tagCompound.setByte(this.generateNBTName("Tick", id), (byte)this.arrowTick);
    }

    protected void loadTick(NBTTagCompound tagCompound, int id)
    {
        this.arrowTick = tagCompound.getByte(this.generateNBTName("Tick", id));
    }

    public boolean haveSupplies()
    {
        return this.hasProjectileItem();
    }
}
