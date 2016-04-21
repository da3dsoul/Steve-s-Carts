package vswe.stevescarts.Helpers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Modules.IActivatorModule;
import vswe.stevescarts.Modules.ISuppliesModule;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.ModuleChunkLoader;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModulePowerObserver;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Realtimers.ModuleCage;
import vswe.stevescarts.Modules.Realtimers.ModuleCakeServer;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTank;
import vswe.stevescarts.Modules.Workers.ModuleBridge;
import vswe.stevescarts.Modules.Workers.ModuleFertilizer;
import vswe.stevescarts.Modules.Workers.ModuleRailer;
import vswe.stevescarts.Modules.Workers.ModuleTorch;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutter;

public class ModuleState
{
    private static HashMap<Byte, ModuleState> states = new HashMap();
    private Class <? extends ModuleBase > moduleClass;
    private Localization.GUI.DETECTOR name;
    private byte id;
    private ModuleState.STATETYPE type;

    public static HashMap<Byte, ModuleState> getStates()
    {
        return states;
    }

    public static Collection<ModuleState> getStateList()
    {
        return states.values();
    }

    public ModuleState(int id, Class <? extends ModuleBase > moduleClass, Localization.GUI.DETECTOR name, ModuleState.STATETYPE type)
    {
        this.moduleClass = moduleClass;
        this.name = name;
        this.id = (byte)id;
        this.type = type;
        states.put(Byte.valueOf(this.id), this);
    }

    public boolean evaluate(MinecartModular cart)
    {
        Iterator passenger2;
        Iterator hasModule1;
        ModuleBase hasModule2;
        ModuleBase i$1;

        switch (ModuleState.NamelessClass1312965292.$SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE[this.type.ordinal()])
        {
            case 1:
                passenger2 = cart.getModules().iterator();

                do
                {
                    if (!passenger2.hasNext())
                    {
                        return false;
                    }

                    hasModule2 = (ModuleBase)passenger2.next();
                }
                while (!this.isModuleOfCorrectType(hasModule2) || !(hasModule2 instanceof ISuppliesModule));

                return ((ISuppliesModule)hasModule2).haveSupplies();

            case 2:
                passenger2 = cart.getModules().iterator();

                do
                {
                    if (!passenger2.hasNext())
                    {
                        return false;
                    }

                    hasModule2 = (ModuleBase)passenger2.next();
                }
                while (!this.isModuleOfCorrectType(hasModule2) || !(hasModule2 instanceof IActivatorModule));

                return ((IActivatorModule)hasModule2).isActive(0);

            case 3:
                if (this instanceof ModuleState.ModuleStateInv)
                {
                    boolean passenger1 = false;
                    hasModule1 = cart.getModules().iterator();

                    while (hasModule1.hasNext())
                    {
                        i$1 = (ModuleBase)hasModule1.next();

                        if (this.isModuleOfCorrectType(i$1))
                        {
                            ModuleChest module1 = (ModuleChest)i$1;

                            if (((ModuleState.ModuleStateInv)this).full && !module1.isCompletelyFilled())
                            {
                                return false;
                            }

                            if (!((ModuleState.ModuleStateInv)this).full && !module1.isCompletelyEmpty())
                            {
                                return false;
                            }

                            passenger1 = true;
                        }
                    }

                    return passenger1;
                }

                break;

            case 4:
                Entity passenger = cart.riddenByEntity;

                if (passenger != null)
                {
                    return ((ModuleState.ModuleStatePassenger)this).passengerClass.isAssignableFrom(passenger.getClass()) && ((ModuleState.ModuleStatePassenger)this).isPassengerValid(passenger);
                }

                break;

            case 5:
                hasModule1 = cart.getModules().iterator();

                do
                {
                    if (!hasModule1.hasNext())
                    {
                        return false;
                    }

                    i$1 = (ModuleBase)hasModule1.next();
                }
                while (!this.isModuleOfCorrectType(i$1));

                return ((ModulePowerObserver)i$1).isAreaActive(((ModuleState.ModuleStatePower)this).areaId);

            case 6:
                if (this instanceof ModuleState.ModuleStateTank)
                {
                    boolean hasModule = false;
                    Iterator i$ = cart.getModules().iterator();

                    while (i$.hasNext())
                    {
                        ModuleBase module = (ModuleBase)i$.next();

                        if (this.isModuleOfCorrectType(module))
                        {
                            ModuleTank tank = (ModuleTank)module;
                            boolean result;

                            if (((ModuleState.ModuleStateTank)this).full)
                            {
                                result = tank.isCompletelyFilled();
                            }
                            else
                            {
                                result = tank.isCompletelyEmpty();
                            }

                            if (result == ((ModuleState.ModuleStateTank)this).individual)
                            {
                                return result;
                            }

                            hasModule = !((ModuleState.ModuleStateTank)this).individual;
                        }
                    }

                    return hasModule;
                }
        }

        return false;
    }

    private boolean isModuleOfCorrectType(ModuleBase module)
    {
        return this.moduleClass.isAssignableFrom(module.getClass());
    }

    public String getName()
    {
        return this.name.translate(new String[0]);
    }

    public byte getID()
    {
        return this.id;
    }

    static
    {
        new ModuleState(0, ModuleRailer.class, Localization.GUI.DETECTOR.RAIL, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(1, ModuleTorch.class, Localization.GUI.DETECTOR.TORCH, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(2, ModuleWoodcutter.class, Localization.GUI.DETECTOR.SAPLING, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(3, ModuleFarmer.class, Localization.GUI.DETECTOR.SEED, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(5, ModuleBridge.class, Localization.GUI.DETECTOR.BRIDGE, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(40, ModuleShooter.class, Localization.GUI.DETECTOR.PROJECTILE, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(41, ModuleFertilizer.class, Localization.GUI.DETECTOR.FERTILIZING, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(49, ModuleCakeServer.class, Localization.GUI.DETECTOR.CAKE, ModuleState.STATETYPE.SUPPLY);
        new ModuleState(6, ModuleShield.class, Localization.GUI.DETECTOR.SHIELD, ModuleState.STATETYPE.ACTIVATION);
        new ModuleState(7, ModuleChunkLoader.class, Localization.GUI.DETECTOR.CHUNK, ModuleState.STATETYPE.ACTIVATION);
        new ModuleState(8, ModuleInvisible.class, Localization.GUI.DETECTOR.INVISIBILITY, ModuleState.STATETYPE.ACTIVATION);
        new ModuleState(9, ModuleDrill.class, Localization.GUI.DETECTOR.DRILL, ModuleState.STATETYPE.ACTIVATION);
        new ModuleState(12, ModuleCage.class, Localization.GUI.DETECTOR.CAGE, ModuleState.STATETYPE.ACTIVATION);
        new ModuleState.ModuleStateInv(10, Localization.GUI.DETECTOR.STORAGE_FULL, true);
        new ModuleState.ModuleStateInv(11, Localization.GUI.DETECTOR.STORAGE_EMPTY, false);
        new ModuleState.ModuleStatePassenger(13, Localization.GUI.DETECTOR.PASSENGER, EntityLiving.class);
        new ModuleState.ModuleStatePassenger(14, Localization.GUI.DETECTOR.ANIMAL, IAnimals.class);
        new ModuleState.ModuleStatePassenger(15, Localization.GUI.DETECTOR.TAMEABLE, EntityTameable.class);
        new ModuleState.ModuleStatePassenger(16, Localization.GUI.DETECTOR.BREEDABLE, EntityAgeable.class);
        new ModuleState.ModuleStatePassenger(17, Localization.GUI.DETECTOR.HOSTILE, IMob.class);
        new ModuleState.ModuleStatePassenger(18, Localization.GUI.DETECTOR.CREEPER, EntityCreeper.class);
        new ModuleState.ModuleStatePassenger(19, Localization.GUI.DETECTOR.SKELETON, EntitySkeleton.class);
        new ModuleState.ModuleStatePassenger(20, Localization.GUI.DETECTOR.SPIDER, EntitySpider.class);
        new ModuleState.ModuleStatePassenger(21, Localization.GUI.DETECTOR.ZOMBIE, EntityZombie.class);
        new ModuleState.ModuleStatePassenger(22, Localization.GUI.DETECTOR.PIG_MAN, EntityPigZombie.class);
        new ModuleState.ModuleStatePassenger(23, Localization.GUI.DETECTOR.SILVERFISH, EntitySilverfish.class);
        new ModuleState.ModuleStatePassenger(24, Localization.GUI.DETECTOR.BLAZE, EntityBlaze.class);
        new ModuleState.ModuleStatePassenger(25, Localization.GUI.DETECTOR.BAT, EntityBat.class);
        new ModuleState.ModuleStatePassenger(26, Localization.GUI.DETECTOR.WITCH, EntityWitch.class);
        new ModuleState.ModuleStatePassenger(27, Localization.GUI.DETECTOR.PIG, EntityPig.class);
        new ModuleState.ModuleStatePassenger(28, Localization.GUI.DETECTOR.SHEEP, EntitySheep.class);
        new ModuleState.ModuleStatePassenger(29, Localization.GUI.DETECTOR.COW, EntityCow.class);
        new ModuleState.ModuleStatePassenger(30, Localization.GUI.DETECTOR.MOOSHROOM, EntityMooshroom.class);
        new ModuleState.ModuleStatePassenger(31, Localization.GUI.DETECTOR.CHICKEN, EntityChicken.class);
        new ModuleState.ModuleStatePassenger(32, Localization.GUI.DETECTOR.WOLF, EntityWolf.class);
        new ModuleState.ModuleStatePassenger(33, Localization.GUI.DETECTOR.SNOW_GOLEM, EntitySnowman.class);
        new ModuleState.ModuleStatePassenger(34, Localization.GUI.DETECTOR.OCELOT, EntityOcelot.class);
        new ModuleState.ModuleStatePassenger(35, Localization.GUI.DETECTOR.VILLAGER, EntityVillager.class);
        new ModuleState.ModuleStatePassenger(36, Localization.GUI.DETECTOR.PLAYER, EntityPlayer.class);
        ModuleState.ModuleStatePassenger var10001 = new ModuleState.ModuleStatePassenger(37, Localization.GUI.DETECTOR.ZOMBIE, EntityZombie.class)
        {
            public boolean isPassengerValid(Entity passenger)
            {
                return ((EntityZombie)passenger).isVillager();
            }
        };
        var10001 = new ModuleState.ModuleStatePassenger(38, Localization.GUI.DETECTOR.CHILD, EntityAgeable.class)
        {
            public boolean isPassengerValid(Entity passenger)
            {
                return ((EntityAgeable)passenger).isChild();
            }
        };
        var10001 = new ModuleState.ModuleStatePassenger(39, Localization.GUI.DETECTOR.TAMED, EntityTameable.class)
        {
            public boolean isPassengerValid(Entity passenger)
            {
                return ((EntityTameable)passenger).isTamed();
            }
        };
        new ModuleState.ModuleStatePower(42, Localization.GUI.DETECTOR.POWER_RED, 0);
        new ModuleState.ModuleStatePower(43, Localization.GUI.DETECTOR.POWER_BLUE, 1);
        new ModuleState.ModuleStatePower(44, Localization.GUI.DETECTOR.POWER_GREEN, 2);
        new ModuleState.ModuleStatePower(45, Localization.GUI.DETECTOR.POWER_YELLOW, 3);
        new ModuleState.ModuleStateTank(46, Localization.GUI.DETECTOR.TANKS_FULL, true, false);
        new ModuleState.ModuleStateTank(47, Localization.GUI.DETECTOR.TANKS_EMPTY, false, false);
        new ModuleState.ModuleStateTank(48, Localization.GUI.DETECTOR.TANK_EMPTY, false, true);
    }

    static class NamelessClass1312965292
    {
        static final int[] $SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE = new int[ModuleState.STATETYPE.values().length];

        static
        {
            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE[ModuleState.STATETYPE.SUPPLY.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE[ModuleState.STATETYPE.ACTIVATION.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE[ModuleState.STATETYPE.INVENTORY.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE[ModuleState.STATETYPE.PASSENGER.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE[ModuleState.STATETYPE.POWER.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                $SwitchMap$vswe$stevescarts$Helpers$ModuleState$STATETYPE[ModuleState.STATETYPE.TANK.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }

    private static class ModuleStateInv extends ModuleState
    {
        private boolean full;

        public ModuleStateInv(int id, Localization.GUI.DETECTOR name, boolean full)
        {
            super(id, ModuleChest.class, name, ModuleState.STATETYPE.INVENTORY);
            this.full = full;
        }
    }

    private static class ModuleStatePassenger extends ModuleState
    {
        private Class passengerClass;

        public ModuleStatePassenger(int id, Localization.GUI.DETECTOR name, Class passengerClass)
        {
            super(id, (Class)null, name, ModuleState.STATETYPE.PASSENGER);
            this.passengerClass = passengerClass;
        }

        public boolean isPassengerValid(Entity passenger)
        {
            return true;
        }
    }

    private static class ModuleStatePower extends ModuleState
    {
        private int areaId;

        public ModuleStatePower(int id, Localization.GUI.DETECTOR name, int areaId)
        {
            super(id, ModulePowerObserver.class, name, ModuleState.STATETYPE.POWER);
            this.areaId = areaId;
        }
    }

    private static class ModuleStateTank extends ModuleState
    {
        private boolean full;
        private boolean individual;

        public ModuleStateTank(int id, Localization.GUI.DETECTOR name, boolean full, boolean individual)
        {
            super(id, ModuleTank.class, name, ModuleState.STATETYPE.TANK);
            this.full = full;
            this.individual = individual;
        }
    }

    public static enum STATETYPE
    {
        SUPPLY("SUPPLY", 0),
        ACTIVATION("ACTIVATION", 1),
        INVENTORY("INVENTORY", 2),
        PASSENGER("PASSENGER", 3),
        POWER("POWER", 4),
        TANK("TANK", 5);

        private static final ModuleState.STATETYPE[] $VALUES = new ModuleState.STATETYPE[]{SUPPLY, ACTIVATION, INVENTORY, PASSENGER, POWER, TANK};

        private STATETYPE(String var1, int var2) {}
    }
}
