package vswe.stevescarts.Upgrades;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Blocks.ModBlocks;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.RecipeHelper;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.TileEntities.TileEntityUpgrade;

public class AssemblerUpgrade
{
    private static HashMap<Byte, AssemblerUpgrade> upgrades = new HashMap();
    private static HashMap<Byte, IIcon> sides = new HashMap();
    private byte id;
    private int sideTexture;
    private String name;
    private ArrayList<BaseEffect> effects;
    private IIcon icon;

    public static HashMap<Byte, AssemblerUpgrade> getUpgrades()
    {
        return upgrades;
    }

    public static Collection<AssemblerUpgrade> getUpgradesList()
    {
        return upgrades.values();
    }

    public static AssemblerUpgrade getUpgrade(int id)
    {
        return (AssemblerUpgrade)upgrades.get(Byte.valueOf((byte)id));
    }

    public static void init()
    {
        AssemblerUpgrade batteries = (new AssemblerUpgrade(0, "Batteries")).addEffect(new FuelCapacity(5000)).addEffect(new Recharger(40)).addRecipe(new Object[][] {{Items.redstone, Items.redstone, Items.redstone}, {Items.redstone, Items.diamond, Items.redstone}, {Items.redstone, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.redstone}});
        (new AssemblerUpgrade(1, "Power Crystal")).addEffect(new FuelCapacity(15000)).addEffect(new Recharger(150)).addRecipe(new Object[][] {{Items.diamond, Items.glowstone_dust, Items.diamond}, {Items.glowstone_dust, Blocks.emerald_block, Items.glowstone_dust}, {Items.diamond, batteries.getItemStack(), Items.diamond}});
        AssemblerUpgrade knowledge = (new AssemblerUpgrade(2, "Module knowledge")).addEffect(new TimeFlat(-750)).addEffect(new TimeFlatCart(-5000)).addEffect(new WorkEfficiency(-0.01F)).addRecipe(new Object[][] {{Items.book, Blocks.bookshelf, Items.book}, {Blocks.bookshelf, Blocks.enchanting_table, Blocks.bookshelf}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}});
        (new AssemblerUpgrade(3, "Industrial espionage")).addEffect(new TimeFlat(-2500)).addEffect(new TimeFlatCart(-14000)).addEffect(new WorkEfficiency(-0.01F)).addRecipe(new Object[][] {{Blocks.bookshelf, ComponentTypes.REINFORCED_METAL.getItemStack(), Blocks.bookshelf}, {ComponentTypes.EYE_OF_GALGADOR.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.EYE_OF_GALGADOR.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), knowledge.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}});
        ItemStack[] books = new ItemStack[5];

        for (int experienced = 0; experienced < 5; ++experienced)
        {
            books[experienced] = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(Enchantment.efficiency, experienced + 1));
        }

        AssemblerUpgrade var5 = (new AssemblerUpgrade(4, "Experienced assembler")).addEffect(new WorkEfficiency(0.1F)).addEffect(new FuelCost(0.3F)).addRecipe(new Object[][] {{ComponentTypes.SIMPLE_PCB.getItemStack(), books[0], ComponentTypes.SIMPLE_PCB.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.ADVANCED_PCB.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}}).addRecipe(new Object[][] {{Items.redstone, books[1], Items.redstone}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.ADVANCED_PCB.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}}).addRecipe(new Object[][] {{Items.redstone, books[2], Items.redstone}, {Items.iron_ingot, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.iron_ingot}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}}).addRecipe(new Object[][] {{null, books[3], null}, {Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}}).addRecipe(new Object[][] {{null, books[4], null}, {null, Items.redstone, null}, {Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}});
        (new AssemblerUpgrade(5, "New Era")).addEffect(new WorkEfficiency(1.0F)).addEffect(new FuelCost(30.0F)).addRecipe(new Object[][] {{ComponentTypes.GALGADORIAN_METAL.getItemStack(), books[4], ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.ADVANCED_PCB, ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {ComponentTypes.GALGADORIAN_METAL.getItemStack(), var5.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack()}});
        (new AssemblerUpgrade(6, "CO2 friendly")).addEffect(new FuelCost(-0.15F)).addRecipe(new Object[][] {{null, Blocks.piston, null}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.fence, ComponentTypes.SIMPLE_PCB.getItemStack()}, {ComponentTypes.CLEANING_FAN, ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.CLEANING_FAN}});
        (new AssemblerUpgrade(7, "Generic engine")).addEffect(new CombustionFuel()).addEffect(new FuelCost(0.05F)).addRecipe(new Object[][] {{null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}, {Blocks.piston, Blocks.furnace, Blocks.piston}, {Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}});
        (new AssemblerUpgrade(8, "Module input", 1)).addEffect(new InputChest(7, 3)).addRecipe(new Object[][] {{null, ComponentTypes.ADVANCED_PCB.getItemStack(), null}, {Blocks.piston, Blocks.chest, Blocks.piston}, {Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}});
        (new AssemblerUpgrade(9, "Production line")).addEffect(new Blueprint()).addRecipe(new Object[][] {{null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}, {ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.BLANK_UPGRADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}});
        (new AssemblerUpgrade(10, "Cart Deployer")).addEffect(new Deployer()).addRecipe(new Object[][] {{Items.iron_ingot, Blocks.rail, Items.iron_ingot}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.piston, ComponentTypes.SIMPLE_PCB.getItemStack()}, {Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}});
        (new AssemblerUpgrade(11, "Cart Modifier")).addEffect(new Disassemble()).addRecipe(new Object[][] {{Items.iron_ingot, null, Items.iron_ingot}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.anvil, ComponentTypes.SIMPLE_PCB.getItemStack()}, {Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}});
        (new AssemblerUpgrade(12, "Cart Crane")).addEffect(new Transposer()).addRecipe(new Object[][] {{Blocks.piston, Blocks.rail, Blocks.piston}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack()}, {null, ComponentTypes.BLANK_UPGRADE.getItemStack(), null}});
        (new AssemblerUpgrade(13, "Redstone Control")).addEffect(new Redstone()).addRecipe(new Object[][] {{ComponentTypes.SIMPLE_PCB.getItemStack(), Items.repeater, ComponentTypes.SIMPLE_PCB.getItemStack()}, {Blocks.redstone_torch, ComponentTypes.ADVANCED_PCB.getItemStack(), Blocks.redstone_torch}, {Items.redstone, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.redstone}});
        (new AssemblerUpgrade(14, "Creative Mode")).addEffect(new WorkEfficiency(10000.0F)).addEffect(new FuelCost(-1.0F));
        AssemblerUpgrade demolisher = (new AssemblerUpgrade(15, "Quick Demolisher")).addEffect(new TimeFlatRemoved(-8000)).addRecipe(new Object[][] {{Blocks.obsidian, ComponentTypes.REINFORCED_METAL.getItemStack(), Blocks.obsidian}, {ComponentTypes.REINFORCED_METAL.getItemStack(), Blocks.iron_block, ComponentTypes.REINFORCED_METAL.getItemStack()}, {Blocks.obsidian, ComponentTypes.BLANK_UPGRADE.getItemStack(), Blocks.obsidian}});
        (new AssemblerUpgrade(16, "Entropy")).addEffect(new TimeFlatRemoved(-32000)).addEffect(new TimeFlat(3000)).addRecipe(new Object[][] {{ComponentTypes.EYE_OF_GALGADOR.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), null}, {Items.diamond, Blocks.lapis_block, Items.diamond}, {null, demolisher.getItemStack(), ComponentTypes.EYE_OF_GALGADOR.getItemStack()}});
        (new AssemblerUpgrade(17, "Manager Bridge")).addEffect(new Manager()).addEffect(new TimeFlatCart(200)).addRecipe(new Object[][] {{Items.iron_ingot, Items.ender_pearl, Items.iron_ingot}, {ComponentTypes.SIMPLE_PCB.getItemStack(), ModBlocks.EXTERNAL_DISTRIBUTOR.getBlock(), ComponentTypes.SIMPLE_PCB.getItemStack()}, {Items.iron_ingot, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.iron_ingot}});
        (new AssemblerUpgrade(18, "Thermal Engine Upgrade")).addEffect(new ThermalFuel()).addEffect(new FuelCost(0.05F)).addRecipe(new Object[][] {{Blocks.nether_brick, ComponentTypes.ADVANCED_PCB.getItemStack(), Blocks.nether_brick}, {Blocks.piston, Blocks.furnace, Blocks.piston}, {Blocks.obsidian, ComponentTypes.BLANK_UPGRADE.getItemStack(), Blocks.obsidian}});
        (new AssemblerUpgrade(19, "Solar Panel")).addEffect(new Solar()).addRecipe(new Object[][] {{ComponentTypes.SOLAR_PANEL.getItemStack(), ComponentTypes.SOLAR_PANEL.getItemStack(), ComponentTypes.SOLAR_PANEL.getItemStack()}, {Items.diamond, Items.redstone, Items.diamond}, {Items.redstone, ComponentTypes.BLANK_UPGRADE.getItemStack(), Items.redstone}});
    }

    public static IIcon getStandardIcon()
    {
        return (IIcon)sides.get(Byte.valueOf((byte)0));
    }

    @SideOnly(Side.CLIENT)
    public static void initSides(IIconRegister register)
    {
        ArrayList used = new ArrayList();
        Iterator i$ = getUpgradesList().iterator();

        while (i$.hasNext())
        {
            AssemblerUpgrade upgrade = (AssemblerUpgrade)i$.next();

            if (!used.contains(Integer.valueOf(upgrade.sideTexture)))
            {
                HashMap var10000 = sides;
                Byte var10001 = Byte.valueOf((byte)upgrade.sideTexture);
                StringBuilder var10003 = new StringBuilder();
                StevesCarts.instance.getClass();
                var10000.put(var10001, register.registerIcon(var10003.append("stevescarts").append(":upgrade_side_").append(upgrade.sideTexture).append("_icon").toString()));
                used.add(Integer.valueOf(upgrade.sideTexture));
            }
        }
    }

    public AssemblerUpgrade(int id, String name)
    {
        this(id, name, 0);
    }

    public AssemblerUpgrade(int id, String name, int sideTexture)
    {
        this.id = (byte)id;
        this.sideTexture = sideTexture;
        this.name = name;
        this.effects = new ArrayList();
        upgrades.put(Byte.valueOf(this.id), this);
    }

    public byte getId()
    {
        return this.id;
    }

    public String getName()
    {
        return StatCollector.translateToLocal("item.SC2:" + this.getRawName() + ".name");
    }

    public AssemblerUpgrade addEffect(BaseEffect effect)
    {
        this.effects.add(effect);
        return this;
    }

    public AssemblerUpgrade addRecipe(int resultCount, Object[][] recipe)
    {
        RecipeHelper.addRecipe(this.getItemStack(resultCount), recipe);
        return this;
    }

    public AssemblerUpgrade addRecipe(Object[][] recipe)
    {
        return this.addRecipe(1, recipe);
    }

    protected ItemStack getItemStack()
    {
        return this.getItemStack(1);
    }

    protected ItemStack getItemStack(int count)
    {
        return new ItemStack(ModItems.upgrades, count, this.id);
    }

    public ArrayList<BaseEffect> getEffects()
    {
        return this.effects;
    }

    public boolean useStandardInterface()
    {
        return this.getInterfaceEffect() == null;
    }

    public int getInventorySize()
    {
        InventoryEffect inv = this.getInventoryEffect();
        return inv != null ? inv.getInventorySize() : 0;
    }

    public InterfaceEffect getInterfaceEffect()
    {
        Iterator i$ = this.effects.iterator();
        BaseEffect effect;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            effect = (BaseEffect)i$.next();
        }
        while (!(effect instanceof InterfaceEffect));

        return (InterfaceEffect)effect;
    }

    public InventoryEffect getInventoryEffect()
    {
        Iterator i$ = this.effects.iterator();
        BaseEffect effect;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            effect = (BaseEffect)i$.next();
        }
        while (!(effect instanceof InventoryEffect));

        return (InventoryEffect)effect;
    }

    public TankEffect getTankEffect()
    {
        Iterator i$ = this.effects.iterator();
        BaseEffect effect;

        do
        {
            if (!i$.hasNext())
            {
                return null;
            }

            effect = (BaseEffect)i$.next();
        }
        while (!(effect instanceof TankEffect));

        return (TankEffect)effect;
    }

    public void init(TileEntityUpgrade upgrade)
    {
        Iterator i$ = this.effects.iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();
            effect.init(upgrade);
        }
    }

    public void load(TileEntityUpgrade upgrade, NBTTagCompound compound)
    {
        Iterator i$ = this.effects.iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();
            effect.load(upgrade, compound);
        }
    }

    public void save(TileEntityUpgrade upgrade, NBTTagCompound compound)
    {
        Iterator i$ = this.effects.iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();
            effect.save(upgrade, compound);
        }
    }

    public void update(TileEntityUpgrade upgrade)
    {
        Iterator i$ = this.effects.iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();
            effect.update(upgrade);
        }
    }

    public void removed(TileEntityUpgrade upgrade)
    {
        Iterator i$ = this.effects.iterator();

        while (i$.hasNext())
        {
            BaseEffect effect = (BaseEffect)i$.next();
            effect.removed(upgrade);
        }
    }

    public String getRawName()
    {
        return this.name.replace(":", "").replace(" ", "_").toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    public void createIcon(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.icon = register.registerIcon(var10002.append("stevescarts").append(":").append(this.getRawName().replace("_upgrade", "")).append("_icon").toString());
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        return this.icon;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getMainTexture()
    {
        return this.icon;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getSideTexture()
    {
        return (IIcon)sides.get(Byte.valueOf((byte)this.sideTexture));
    }
}
