package vswe.stevescarts.ModuleData;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.*;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vswe.stevescarts.Modules.Addons.*;
import vswe.stevescarts.Modules.Addons.Plants.ModuleLargeTrees;
import vswe.stevescarts.Modules.Engines.*;
import vswe.stevescarts.Modules.Realtimers.*;
import vswe.stevescarts.Modules.Storages.Chests.*;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.CartVersion;
import vswe.stevescarts.Helpers.ColorHelper;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.RecipeHelper;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.Models.Cart.ModelAdvancedTank;
import vswe.stevescarts.Models.Cart.ModelBridge;
import vswe.stevescarts.Models.Cart.ModelCage;
import vswe.stevescarts.Models.Cart.ModelCake;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelCleaner;
import vswe.stevescarts.Models.Cart.ModelCompactSolarPanel;
import vswe.stevescarts.Models.Cart.ModelDrill;
import vswe.stevescarts.Models.Cart.ModelDynamite;
import vswe.stevescarts.Models.Cart.ModelEggBasket;
import vswe.stevescarts.Models.Cart.ModelEngineFrame;
import vswe.stevescarts.Models.Cart.ModelEngineInside;
import vswe.stevescarts.Models.Cart.ModelExtractingChests;
import vswe.stevescarts.Models.Cart.ModelFarmer;
import vswe.stevescarts.Models.Cart.ModelFrontChest;
import vswe.stevescarts.Models.Cart.ModelFrontTank;
import vswe.stevescarts.Models.Cart.ModelGiftStorage;
import vswe.stevescarts.Models.Cart.ModelGun;
import vswe.stevescarts.Models.Cart.ModelHull;
import vswe.stevescarts.Models.Cart.ModelHullTop;
import vswe.stevescarts.Models.Cart.ModelLawnMower;
import vswe.stevescarts.Models.Cart.ModelLever;
import vswe.stevescarts.Models.Cart.ModelLiquidDrainer;
import vswe.stevescarts.Models.Cart.ModelLiquidSensors;
import vswe.stevescarts.Models.Cart.ModelMobDetector;
import vswe.stevescarts.Models.Cart.ModelNote;
import vswe.stevescarts.Models.Cart.ModelPigHead;
import vswe.stevescarts.Models.Cart.ModelPigHelmet;
import vswe.stevescarts.Models.Cart.ModelPigTail;
import vswe.stevescarts.Models.Cart.ModelPumpkinHull;
import vswe.stevescarts.Models.Cart.ModelPumpkinHullTop;
import vswe.stevescarts.Models.Cart.ModelRailer;
import vswe.stevescarts.Models.Cart.ModelSeat;
import vswe.stevescarts.Models.Cart.ModelShield;
import vswe.stevescarts.Models.Cart.ModelShootingRig;
import vswe.stevescarts.Models.Cart.ModelSideChests;
import vswe.stevescarts.Models.Cart.ModelSideTanks;
import vswe.stevescarts.Models.Cart.ModelSniperRifle;
import vswe.stevescarts.Models.Cart.ModelSolarPanelBase;
import vswe.stevescarts.Models.Cart.ModelSolarPanelHeads;
import vswe.stevescarts.Models.Cart.ModelToolPlate;
import vswe.stevescarts.Models.Cart.ModelTopChest;
import vswe.stevescarts.Models.Cart.ModelTopTank;
import vswe.stevescarts.Models.Cart.ModelTorchplacer;
import vswe.stevescarts.Models.Cart.ModelTrackRemover;
import vswe.stevescarts.Models.Cart.ModelWheel;
import vswe.stevescarts.Models.Cart.ModelWoodCutter;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleAnimal;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleBat;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleMonster;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModulePlayer;
import vswe.stevescarts.Modules.Addons.Mobdetectors.ModuleVillager;
import vswe.stevescarts.Modules.Addons.Plants.ModuleModTrees;
import vswe.stevescarts.Modules.Addons.Plants.ModuleNetherwart;
import vswe.stevescarts.Modules.Addons.Plants.ModulePlantSize;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleCake;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleEgg;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleFireball;
import vswe.stevescarts.Modules.Addons.Projectiles.ModulePotion;
import vswe.stevescarts.Modules.Addons.Projectiles.ModuleSnowball;
import vswe.stevescarts.Modules.Hull.ModuleCheatHull;
import vswe.stevescarts.Modules.Hull.ModuleGalgadorian;
import vswe.stevescarts.Modules.Hull.ModuleHull;
import vswe.stevescarts.Modules.Hull.ModulePig;
import vswe.stevescarts.Modules.Hull.ModulePumpkin;
import vswe.stevescarts.Modules.Hull.ModuleReinforced;
import vswe.stevescarts.Modules.Hull.ModuleStandard;
import vswe.stevescarts.Modules.Hull.ModuleWood;
import vswe.stevescarts.Modules.Storages.ModuleStorage;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleAdvancedTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleCheatTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleFrontTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleInternalTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleOpenTank;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleSideTanks;
import vswe.stevescarts.Modules.Storages.Tanks.ModuleTopTank;
import vswe.stevescarts.Modules.Workers.ModuleBridge;
import vswe.stevescarts.Modules.Workers.ModuleBridgeLarge;
import vswe.stevescarts.Modules.Workers.ModuleFertilizer;
import vswe.stevescarts.Modules.Workers.ModuleHydrater;
import vswe.stevescarts.Modules.Workers.ModuleLiquidDrainer;
import vswe.stevescarts.Modules.Workers.ModuleRailer;
import vswe.stevescarts.Modules.Workers.ModuleRailerLarge;
import vswe.stevescarts.Modules.Workers.ModuleRemover;
import vswe.stevescarts.Modules.Workers.ModuleTorch;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillDiamond;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillGalgadorian;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillHardened;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrillIron;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmerDiamond;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmerGalgadorian;
import vswe.stevescarts.Modules.Workers.Tools.ModuleTool;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutterDiamond;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutterGalgadorian;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutterHardened;

public class ModuleData
{
    private static IdentityHashMap<Byte, ModuleData> moduleList;
    private static Class[] moduleGroups;
    private static Localization.MODULE_INFO[] moduleGroupNames;
    private byte id;
    private Class <? extends ModuleBase > moduleClass;
    private String name;
    private int modularCost;
    private int groupID;
    private ArrayList<ModuleData.SIDE> renderingSides;
    private boolean allowDuplicate;
    private ArrayList<ModuleData> nemesis = null;
    private ArrayList<ModuleDataGroup> requirement = null;
    private ModuleData parent = null;
    private boolean isValid;
    private boolean isLocked;
    private boolean defaultLock;
    private boolean hasRecipe;
    private ArrayList<Localization.MODULE_INFO> message;
    private HashMap<String, ModelCartbase> models;
    private HashMap<String, ModelCartbase> modelsPlaceholder;
    private ArrayList<String> removedModels;
    private float modelMult = 0.75F;
    private boolean useExtraData;
    private byte extraDataDefaultValue;
    private static ArrayList<Object[][]> recipes;
    private static final int MAX_MESSAGE_ROW_LENGTH = 30;
    private IIcon icon;

    public static IdentityHashMap<Byte, ModuleData> getList()
    {
        return moduleList;
    }

    public static Collection<ModuleData> getModules()
    {
        return getList().values();
    }

    public static void init()
    {
        String planks = "plankWood";
        String wood = "logWood";
        ItemStack woodSingleSlab = new ItemStack(Blocks.wooden_slab, 1, -1);
        ItemStack bonemeal = new ItemStack(Items.dye, 1, 15);
        moduleGroups = new Class[] {ModuleHull.class, ModuleEngine.class, ModuleTool.class, ModuleStorage.class, ModuleAddon.class};
        moduleGroupNames = new Localization.MODULE_INFO[] {Localization.MODULE_INFO.HULL_CATEGORY, Localization.MODULE_INFO.ENGINE_CATEGORY, Localization.MODULE_INFO.TOOL_CATEGORY, Localization.MODULE_INFO.STORAGE_CATEGORY, Localization.MODULE_INFO.ADDON_CATEGORY, Localization.MODULE_INFO.ATTACHMENT_CATEGORY};
        moduleList = new IdentityHashMap();
        ModuleDataGroup engineGroup = new ModuleDataGroup(Localization.MODULE_INFO.ENGINE_GROUP);
        ModuleData coalStandard = (new ModuleData(0, "Coal Engine", ModuleCoalStandard.class, 15)).addRecipe(new Object[][] {{Items.iron_ingot, Items.iron_ingot, Items.iron_ingot}, {Items.iron_ingot, Blocks.furnace, Items.iron_ingot}, {Blocks.piston, null, Blocks.piston}});
        ModuleData coalTiny = (new ModuleData(44, "Tiny Coal Engine", ModuleCoalTiny.class, 2)).addRecipe(new Object[][] {{Items.iron_ingot, Blocks.furnace, Items.iron_ingot}, {null, Blocks.piston, null}});
        ModuleData rfengine = (new ModuleData(104, "RF Engine", ModuleRFEngine.class, 2)).addRecipe(new Object[][] {{Items.iron_ingot, Blocks.redstone_block, Items.iron_ingot}, {null, Blocks.piston, null}}).setAllowDuplicate();
        addNemesis(coalTiny, coalStandard);
        ModuleData solar1 = (new ModuleData(1, "Solar Engine", ModuleSolarStandard.class, 20)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.CENTER, ModuleData.SIDE.TOP}).removeModel("Top").addRecipe(new Object[][] {{Items.iron_ingot, ComponentTypes.SOLAR_PANEL.getItemStack(), Items.iron_ingot}, {ComponentTypes.SOLAR_PANEL.getItemStack(), ComponentTypes.ADVANCED_PCB.getItemStack(), ComponentTypes.SOLAR_PANEL.getItemStack()}, {Blocks.piston, ComponentTypes.SOLAR_PANEL.getItemStack(), Blocks.piston}});
        ModuleData solar0 = (new ModuleData(45, "Basic Solar Engine", ModuleSolarBasic.class, 12)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.CENTER, ModuleData.SIDE.TOP}).removeModel("Top").addRecipe(new Object[][] {{ComponentTypes.SOLAR_PANEL.getItemStack(), Items.iron_ingot, ComponentTypes.SOLAR_PANEL.getItemStack()}, {Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot}, {null, Blocks.piston, null}});
        ModuleData compactsolar = (new ModuleData(56, "Compact Solar Engine", ModuleSolarCompact.class, 32)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).addRecipe(new Object[][] {{ComponentTypes.ADVANCED_SOLAR_PANEL.getItemStack(), Items.iron_ingot, ComponentTypes.ADVANCED_SOLAR_PANEL.getItemStack()}, {ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack()}, {Blocks.piston, Items.iron_ingot, Blocks.piston}});
        (new ModuleData(2, "Side Chests", ModuleSideChests.class, 3)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).addRecipe(new Object[][] {{ComponentTypes.HUGE_CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.HUGE_CHEST_PANE.getItemStack()}, {ComponentTypes.LARGE_CHEST_PANE.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.LARGE_CHEST_PANE.getItemStack()}, {ComponentTypes.HUGE_CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.HUGE_CHEST_PANE.getItemStack()}});
        (new ModuleData(3, "Top Chest", ModuleTopChest.class, 6)).addSide(ModuleData.SIDE.TOP).addRecipe(new Object[][] {{ComponentTypes.HUGE_CHEST_PANE.getItemStack(), ComponentTypes.HUGE_CHEST_PANE.getItemStack(), ComponentTypes.HUGE_CHEST_PANE.getItemStack()}, {ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()}, {ComponentTypes.HUGE_CHEST_PANE.getItemStack(), ComponentTypes.HUGE_CHEST_PANE.getItemStack(), ComponentTypes.HUGE_CHEST_PANE.getItemStack()}});
        ModuleData frontChest = (new ModuleData(4, "Front Chest", ModuleFrontChest.class, 5)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.LARGE_CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()}, {ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()}, {ComponentTypes.LARGE_CHEST_PANE.getItemStack(), ComponentTypes.LARGE_CHEST_PANE.getItemStack(), ComponentTypes.LARGE_CHEST_PANE.getItemStack()}});
        (new ModuleData(5, "Internal Storage", ModuleInternalStorage.class, 25)).setAllowDuplicate().addRecipe(new Object[][] {{ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()}, {ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()}, {ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack(), ComponentTypes.CHEST_PANE.getItemStack()}});
        (new ModuleData(103, "Creative Storage", ModuleCreativeStorage.class, 25)).setAllowDuplicate();
        (new ModuleData(6, "Extracting Chests", ModuleExtractingChests.class, 75)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.CENTER, ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).addRecipe(new Object[][] {{ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack()}, {ComponentTypes.LARGE_IRON_PANE.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.LARGE_IRON_PANE.getItemStack()}, {ComponentTypes.HUGE_DYNAMIC_PANE.getItemStack(), ComponentTypes.LARGE_DYNAMIC_PANE.getItemStack(), ComponentTypes.HUGE_DYNAMIC_PANE.getItemStack()}});
        (new ModuleData(7, "Torch Placer", ModuleTorch.class, 14)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).addRecipe(new Object[][] {{ComponentTypes.TRI_TORCH.getItemStack(), null, ComponentTypes.TRI_TORCH.getItemStack()}, {Items.iron_ingot, null, Items.iron_ingot}, {Items.iron_ingot, Items.iron_ingot, Items.iron_ingot}});
        ModuleData drill = (new ModuleDataTool(8, "Basic Drill", ModuleDrillDiamond.class, 10, false)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{Items.iron_ingot, Items.diamond, null}, {null, Items.iron_ingot, Items.diamond}, {Items.iron_ingot, Items.diamond, null}});
        ModuleData ironDrill = (new ModuleDataTool(42, "Iron Drill", ModuleDrillIron.class, 3, false)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{Items.iron_ingot, Items.iron_ingot, null}, {null, Items.iron_ingot, Items.iron_ingot}, {Items.iron_ingot, Items.iron_ingot, null}});
        ModuleData hardeneddrill = (new ModuleDataTool(43, "Hardened Drill", ModuleDrillHardened.class, 45, false)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.HARDENED_MESH.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), null}, {Blocks.diamond_block, drill.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}, {ComponentTypes.HARDENED_MESH.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), null}});
        ModuleData galgdrill = (new ModuleDataTool(9, "Galgadorian Drill", ModuleDrillGalgadorian.class, 150, true)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack(), null}, {Blocks.diamond_block, hardeneddrill.getItemStack(), ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack()}, {ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack(), null}});
        ModuleDataGroup drillGroup = new ModuleDataGroup(Localization.MODULE_INFO.DRILL_GROUP);
        drillGroup.add(drill);
        drillGroup.add(ironDrill);
        drillGroup.add(hardeneddrill);
        drillGroup.add(galgdrill);
        ModuleData railer = (new ModuleData(10, "Railer", ModuleRailer.class, 3)).addRecipe(new Object[][] {{Blocks.stone, Blocks.stone, Blocks.stone}, {Items.iron_ingot, Blocks.rail, Items.iron_ingot}, {Blocks.stone, Blocks.stone, Blocks.stone}});
        ModuleData largerailer = (new ModuleData(11, "Large Railer", ModuleRailerLarge.class, 17)).addRecipe(new Object[][] {{Items.iron_ingot, Items.iron_ingot, Items.iron_ingot}, {railer.getItemStack(), Blocks.rail, railer.getItemStack()}, {Items.iron_ingot, Items.iron_ingot, Items.iron_ingot}});
        addNemesis(railer, largerailer);
        ModuleData bridge = (new ModuleData(12, "Bridge Builder", ModuleBridge.class, 14)).addRecipe(new Object[][] {{null, Items.redstone, null}, {Blocks.brick_block, ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.brick_block}, {null, Blocks.piston, null}});
        ModuleData largebridge = (new ModuleData(17, "Large Builder", ModuleBridgeLarge.class, 15)).addRecipe(new Object[][] {{null, Items.redstone, null}, {Blocks.brick_block, ComponentTypes.ADVANCED_PCB.getItemStack(), Blocks.brick_block}, {null, Blocks.piston, null}});
        addNemesis(bridge, largebridge);
        ModuleData remover = (new ModuleData(13, "Track Remover", ModuleRemover.class, 8)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.TOP, ModuleData.SIDE.BACK}).addRecipe(new Object[][] {{Items.iron_ingot, Items.iron_ingot, Items.iron_ingot}, {Items.iron_ingot, null, Items.iron_ingot}, {Items.iron_ingot, null, null}});
        addNemesis(remover,railer);
        addNemesis(remover,largerailer);
        ModuleDataGroup farmerGroup = new ModuleDataGroup(Localization.MODULE_INFO.FARMER_GROUP);
        ModuleData farmerbasic = (new ModuleDataTool(14, "Basic Farmer", ModuleFarmerDiamond.class, 36, false)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{Items.diamond, Items.diamond, Items.diamond}, {null, Items.iron_ingot, null}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.gold_ingot, ComponentTypes.SIMPLE_PCB.getItemStack()}});
        ModuleData farmergalg = (new ModuleDataTool(84, "Galgadorian Farmer", ModuleFarmerGalgadorian.class, 55, true)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {null, ComponentTypes.REINFORCED_METAL.getItemStack(), null}, {ComponentTypes.ADVANCED_PCB.getItemStack(), Items.gold_ingot, ComponentTypes.ADVANCED_PCB.getItemStack()}}).addRecipe(new Object[][] {{ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {null, farmerbasic.getItemStack(), null}, {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}});
        farmerGroup.add(farmerbasic);
        farmerGroup.add(farmergalg);
        ModuleDataGroup woodcutterGroup = new ModuleDataGroup(Localization.MODULE_INFO.CUTTER_GROUP);
        ModuleData woodcutter = (new ModuleDataTool(15, "Basic Wood Cutter", ModuleWoodcutterDiamond.class, 34, false)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.SAW_BLADE.getItemStack(), ComponentTypes.SAW_BLADE.getItemStack(), ComponentTypes.SAW_BLADE.getItemStack()}, {ComponentTypes.SAW_BLADE.getItemStack(), Items.iron_ingot, ComponentTypes.SAW_BLADE.getItemStack()}, {null, ComponentTypes.WOOD_CUTTING_CORE.getItemStack(), null}});
        ModuleData woodcutterHardened = (new ModuleDataTool(79, "Hardened Wood Cutter", ModuleWoodcutterHardened.class, 65, false)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.HARDENED_SAW_BLADE.getItemStack(), ComponentTypes.HARDENED_SAW_BLADE.getItemStack(), ComponentTypes.HARDENED_SAW_BLADE.getItemStack()}, {ComponentTypes.HARDENED_SAW_BLADE.getItemStack(), Items.diamond, ComponentTypes.HARDENED_SAW_BLADE.getItemStack()}, {null, ComponentTypes.WOOD_CUTTING_CORE.getItemStack(), null}}).addRecipe(new Object[][] {{ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), Items.iron_ingot, ComponentTypes.REINFORCED_METAL.getItemStack()}, {null, woodcutter.getItemStack(), null}});
        ModuleData woodcutterGalgadorian = (new ModuleDataTool(80, "Galgadorian Wood Cutter", ModuleWoodcutterGalgadorian.class, 120, true)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.GALGADORIAN_SAW_BLADE.getItemStack(), ComponentTypes.GALGADORIAN_SAW_BLADE.getItemStack(), ComponentTypes.GALGADORIAN_SAW_BLADE.getItemStack()}, {ComponentTypes.GALGADORIAN_SAW_BLADE.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.GALGADORIAN_SAW_BLADE.getItemStack()}, {null, ComponentTypes.WOOD_CUTTING_CORE.getItemStack(), null}}).addRecipe(new Object[][] {{ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {ComponentTypes.GALGADORIAN_METAL.getItemStack(), Items.iron_ingot, ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {null, woodcutterHardened.getItemStack(), null}});
        woodcutterGroup.add(woodcutter);
        woodcutterGroup.add(woodcutterHardened);
        woodcutterGroup.add(woodcutterGalgadorian);
        ModuleDataGroup tankGroup = new ModuleDataGroup(Localization.MODULE_INFO.TANK_GROUP);
        (new ModuleData(16, "Hydrator", ModuleHydrater.class, 6)).addRequirement(tankGroup).addRecipe(new Object[][] {{Items.iron_ingot, Items.glass_bottle, Items.iron_ingot}, {null, Blocks.fence, null}});
        (new ModuleData(18, "Fertilizer", ModuleFertilizer.class, 10)).addRecipe(new Object[][] {{bonemeal, null, bonemeal}, {Items.glass_bottle, Items.leather, Items.glass_bottle}, {Items.leather, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.leather}});
        (new ModuleData(19, "Height Controller", ModuleHeightControl.class, 20)).addRecipe(new Object[][] {{null, Items.compass, null}, {Items.paper, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.paper}, {Items.paper, Items.paper, Items.paper}});
        ModuleData liquidsensors = (new ModuleData(20, "Liquid Sensors", ModuleLiquidSensors.class, 27)).addRequirement(drillGroup).addRecipe(new Object[][] {{Items.redstone, null, Items.redstone}, {Items.lava_bucket, Items.diamond, Items.water_bucket}, {Items.iron_ingot, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.iron_ingot}});
        ModuleData seat = (new ModuleData(25, "Seat", ModuleSeat.class, 3)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.CENTER, ModuleData.SIDE.TOP}).addRecipe(new Object[][] {{null, planks}, {null, planks}, {woodSingleSlab, planks}});
        (new ModuleData(26, "Brake Handle", ModuleBrake.class, 12)).addSide(ModuleData.SIDE.RIGHT).addParent(seat).addRecipe(new Object[][] {{null, null, new ItemStack(Items.dye, 1, 1)}, {Items.iron_ingot, ComponentTypes.REFINED_HANDLE.getItemStack(), null}, {Items.redstone, Items.iron_ingot, null}});
        (new ModuleData(27, "Advanced Control System", ModuleAdvControl.class, 38)).addSide(ModuleData.SIDE.RIGHT).addParent(seat).addRecipe(new Object[][] {{null, ComponentTypes.GRAPHICAL_INTERFACE.getItemStack(), null}, {Items.redstone, ComponentTypes.WHEEL.getItemStack(), Items.redstone}, {Items.iron_ingot, Items.iron_ingot, ComponentTypes.SPEED_HANDLE.getItemStack()}});
        ModuleDataGroup detectorGroup = new ModuleDataGroup(Localization.MODULE_INFO.ENTITY_GROUP);
        ModuleData shooter = (new ModuleData(28, "Shooter", ModuleShooter.class, 15)).addSide(ModuleData.SIDE.TOP).addRecipe(new Object[][] {{ComponentTypes.PIPE.getItemStack(), ComponentTypes.PIPE.getItemStack(), ComponentTypes.PIPE.getItemStack()}, {ComponentTypes.PIPE.getItemStack(), ComponentTypes.SHOOTING_STATION.getItemStack(), ComponentTypes.PIPE.getItemStack()}, {ComponentTypes.PIPE.getItemStack(), ComponentTypes.PIPE.getItemStack(), ComponentTypes.PIPE.getItemStack()}});
        ModuleData advshooter = (new ModuleData(29, "Advanced Shooter", ModuleShooterAdv.class, 50)).addSide(ModuleData.SIDE.TOP).addRequirement(detectorGroup).addRecipe(new Object[][] {{null, ComponentTypes.ENTITY_SCANNER.getItemStack(), null}, {null, ComponentTypes.SHOOTING_STATION.getItemStack(), ComponentTypes.PIPE.getItemStack()}, {Items.iron_ingot, ComponentTypes.ENTITY_ANALYZER.getItemStack(), Items.iron_ingot}});
        ModuleDataGroup shooterGroup = new ModuleDataGroup(Localization.MODULE_INFO.SHOOTER_GROUP);
        shooterGroup.add(shooter);
        shooterGroup.add(advshooter);
        ModuleData animal = (new ModuleData(21, "Entity Detector: Animal", ModuleAnimal.class, 1)).addParent(advshooter).addRecipe(new Object[][] {{Items.porkchop}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        ModuleData player = (new ModuleData(22, "Entity Detector: Player", ModulePlayer.class, 7)).addParent(advshooter).addRecipe(new Object[][] {{Items.diamond}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        ModuleData villager = (new ModuleData(23, "Entity Detector: Villager", ModuleVillager.class, 1)).addParent(advshooter).addRecipe(new Object[][] {{Items.emerald}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        ModuleData monster = (new ModuleData(24, "Entity Detector: Monster", ModuleMonster.class, 1)).addParent(advshooter).addRecipe(new Object[][] {{Items.slime_ball}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        ModuleData bats = (new ModuleData(48, "Entity Detector: Bat", ModuleBat.class, 1)).addParent(advshooter).addRecipe(new Object[][] {{Blocks.pumpkin}, {ComponentTypes.EMPTY_DISK.getItemStack()}});

        detectorGroup.add(animal);
        detectorGroup.add(player);
        detectorGroup.add(villager);
        detectorGroup.add(monster);
        detectorGroup.add(bats);
        ModuleData cleaner = (new ModuleData(30, "Cleaning Machine", ModuleCleaner.class, 23)).addSide(ModuleData.SIDE.CENTER).addRecipe(new Object[][] {{ComponentTypes.CLEANING_TUBE.getItemStack(), ComponentTypes.CLEANING_CORE.getItemStack(), ComponentTypes.CLEANING_TUBE.getItemStack()}, {ComponentTypes.CLEANING_TUBE.getItemStack(), null, ComponentTypes.CLEANING_TUBE.getItemStack()}, {ComponentTypes.CLEANING_TUBE.getItemStack(), null, ComponentTypes.CLEANING_TUBE.getItemStack()}});
        addNemesis(frontChest, cleaner);
        (new ModuleData(31, "Dynamite Carrier", ModuleDynamite.class, 3)).addSide(ModuleData.SIDE.TOP).addRecipe(new Object[][] {{null, ComponentTypes.DYNAMITE.getItemStack(), null}, {ComponentTypes.DYNAMITE.getItemStack(), Items.flint_and_steel, ComponentTypes.DYNAMITE.getItemStack()}, {null, ComponentTypes.DYNAMITE.getItemStack(), null}});
        (new ModuleData(32, "Divine Shield", ModuleShield.class, 60)).addRecipe(new Object[][] {{Blocks.obsidian, ComponentTypes.REFINED_HARDENER.getItemStack(), Blocks.obsidian}, {ComponentTypes.REFINED_HARDENER.getItemStack(), Blocks.diamond_block, ComponentTypes.REFINED_HARDENER.getItemStack()}, {Blocks.obsidian, ComponentTypes.REFINED_HARDENER.getItemStack(), Blocks.obsidian}});
        ModuleData melter = (new ModuleData(33, "Melter", ModuleMelter.class, 10)).addRecipe(new Object[][] {{Blocks.nether_brick, Blocks.glowstone, Blocks.nether_brick}, {Items.glowstone_dust, Blocks.furnace, Items.glowstone_dust}, {Blocks.nether_brick, Blocks.glowstone, Blocks.nether_brick}});
        ModuleData extrememelter = (new ModuleData(34, "Extreme Melter", ModuleMelterExtreme.class, 19)).addRecipe(new Object[][] {{Blocks.nether_brick, Blocks.obsidian, Blocks.nether_brick}, {melter.getItemStack(), Items.lava_bucket, melter.getItemStack()}, {Blocks.nether_brick, Blocks.obsidian, Blocks.nether_brick}});
        addNemesis(melter, extrememelter);
        (new ModuleData(36, "Invisibility Core", ModuleInvisible.class, 21)).addRecipe(new Object[][] {{null, ComponentTypes.GLASS_O_MAGIC.getItemStack(), null}, {ComponentTypes.GLASS_O_MAGIC.getItemStack(), Items.ender_eye, ComponentTypes.GLASS_O_MAGIC.getItemStack()}, {null, Items.golden_carrot, null}});
        (new ModuleDataHull(37, "Wooden Hull", ModuleWood.class)).setCapacity(50).setEngineMax(1).setAddonMax(0).setComplexityMax(15).addRecipe(new Object[][] {{planks, null, planks}, {planks, planks, planks}, {ComponentTypes.WOODEN_WHEELS.getItemStack(), null, ComponentTypes.WOODEN_WHEELS.getItemStack()}});
        (new ModuleDataHull(38, "Standard Hull", ModuleStandard.class)).setCapacity(200).setEngineMax(3).setAddonMax(6).setComplexityMax(50).addRecipe(new Object[][] {{Items.iron_ingot, null, Items.iron_ingot}, {Items.iron_ingot, Items.iron_ingot, Items.iron_ingot}, {ComponentTypes.IRON_WHEELS.getItemStack(), null, ComponentTypes.IRON_WHEELS.getItemStack()}});
        ModuleData reinfhull = (new ModuleDataHull(39, "Reinforced Hull", ModuleReinforced.class)).setCapacity(500).setEngineMax(5).setAddonMax(12).setComplexityMax(150).addRecipe(new Object[][] {{ComponentTypes.REINFORCED_METAL.getItemStack(), null, ComponentTypes.REINFORCED_METAL.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}, {ComponentTypes.REINFORCED_WHEELS.getItemStack(), null, ComponentTypes.REINFORCED_WHEELS.getItemStack()}});
        ModuleData pumpkinhull = (new ModuleDataHull(47, "Pumpkin chariot", ModulePumpkin.class)).setCapacity(40).setEngineMax(1).setAddonMax(0).setComplexityMax(15).addRecipe(new Object[][] {{planks, null, planks}, {planks, Blocks.pumpkin, planks}, {ComponentTypes.WOODEN_WHEELS.getItemStack(), null, ComponentTypes.WOODEN_WHEELS.getItemStack()}});

        (new ModuleDataHull(62, "Mechanical Pig", ModulePig.class)).setCapacity(150).setEngineMax(2).setAddonMax(4).setComplexityMax(50).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{Items.porkchop, null, Items.porkchop}, {Items.porkchop, Items.porkchop, Items.porkchop}, {ComponentTypes.IRON_WHEELS.getItemStack(), null, ComponentTypes.IRON_WHEELS.getItemStack()}}).addMessage(Localization.MODULE_INFO.PIG_MESSAGE);
        (new ModuleDataHull(76, "Creative Hull", ModuleCheatHull.class)).setCapacity(10000).setEngineMax(5).setAddonMax(12).setComplexityMax(150);
        (new ModuleDataHull(81, "Galgadorian Hull", ModuleGalgadorian.class)).setCapacity(1000).setEngineMax(5).setAddonMax(12).setComplexityMax(150).addRecipe(new Object[][] {{ComponentTypes.GALGADORIAN_METAL.getItemStack(), null, ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack()}, {ComponentTypes.GALGADORIAN_WHEELS.getItemStack(), null, ComponentTypes.GALGADORIAN_WHEELS.getItemStack()}});
        StevesCarts.tabsSC2.setIcon(reinfhull.getItemStack());
        StevesCarts.tabsSC2Components.setIcon(ComponentTypes.REINFORCED_WHEELS.getItemStack());
        (new ModuleData(40, "Note Sequencer", ModuleNote.class, 30)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).addRecipe(new Object[][] {{Blocks.noteblock, null, Blocks.noteblock}, {Blocks.noteblock, Blocks.jukebox, Blocks.noteblock}, {planks, Items.redstone, planks}});
        ModuleData colorizer = (new ModuleData(41, "Colorizer", ModuleColorizer.class, 15)).addRecipe(new Object[][] {{ComponentTypes.RED_PIGMENT.getItemStack(), ComponentTypes.GREEN_PIGMENT.getItemStack(), ComponentTypes.BLUE_PIGMENT.getItemStack()}, {Items.iron_ingot, Items.redstone, Items.iron_ingot}, {null, Items.iron_ingot, null}});
        ModuleData colorRandomizer = (new ModuleData(101, "Color Randomizer", ModuleColorRandomizer.class, 20)).addRecipe(new Object[][] {{colorizer.getItemStack()}, {ComponentTypes.SIMPLE_PCB.getItemStack()}});
        addNemesis(colorizer, colorRandomizer);
        (new ModuleData(49, "Chunk Loader", ModuleChunkLoader.class, 84)).addRecipe(new Object[][] {{null, Items.ender_pearl, null}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack()}, {ComponentTypes.REINFORCED_METAL.getItemStack(), ComponentTypes.ADVANCED_PCB.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}});
        ModuleData gift = (new ModuleData(50, "Gift Storage", ModuleGiftStorage.class, 12)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).useExtraData((byte)1).addRecipe(new Object[][] {{ComponentTypes.YELLOW_GIFT_RIBBON.getItemStack(), null, ComponentTypes.RED_GIFT_RIBBON.getItemStack()}, {ComponentTypes.RED_WRAPPING_PAPER.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.GREEN_WRAPPING_PAPER.getItemStack()}, {ComponentTypes.RED_WRAPPING_PAPER.getItemStack(), ComponentTypes.STUFFED_SOCK.getItemStack(), ComponentTypes.GREEN_WRAPPING_PAPER.getItemStack()}});

        (new ModuleData(51, "Projectile: Potion", ModulePotion.class, 10)).addRequirement(shooterGroup).addRecipe(new Object[][] {{Items.glass_bottle}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        (new ModuleData(52, "Projectile: Fire Charge", ModuleFireball.class, 10)).lockByDefault().addRequirement(shooterGroup).addRecipe(new Object[][] {{Items.fire_charge}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        (new ModuleData(53, "Projectile: Egg", ModuleEgg.class, 10)).addRequirement(shooterGroup).addRecipe(new Object[][] {{Items.egg}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        ModuleData snowballshooter = (new ModuleData(54, "Projectile: Snowball", ModuleSnowball.class, 10)).addRequirement(shooterGroup).addRecipe(new Object[][] {{Items.snowball}, {ComponentTypes.EMPTY_DISK.getItemStack()}});

        ModuleData cake = (new ModuleData(90, "Projectile: Cake", ModuleCake.class, 10)).addRequirement(shooterGroup).lock().addRecipe(new Object[][] {{Items.cake}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        ModuleData snowgenerator = (new ModuleData(55, "Freezer", ModuleSnowCannon.class, 24)).addRecipe(new Object[][] {{Blocks.snow, Items.water_bucket, Blocks.snow}, {Items.water_bucket, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.water_bucket}, {Blocks.snow, Items.water_bucket, Blocks.snow}});

        addNemesis(snowgenerator, melter);
        addNemesis(snowgenerator, extrememelter);
        ModuleData cage = (new ModuleData(57, "Cage", ModuleCage.class, 7)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.TOP, ModuleData.SIDE.CENTER}).addRecipe(new Object[][] {{Blocks.fence, Blocks.fence, Blocks.fence}, {Blocks.fence, ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.fence}, {Blocks.fence, Blocks.fence, Blocks.fence}});
        (new ModuleData(58, "Crop: Nether Wart", ModuleNetherwart.class, 20)).addRequirement(farmerGroup).addRecipe(new Object[][] {{Items.nether_wart}, {ComponentTypes.EMPTY_DISK.getItemStack()}});
        (new ModuleData(59, "Firework display", ModuleFirework.class, 45)).addRecipe(new Object[][] {{Blocks.fence, Blocks.dispenser, Blocks.fence}, {Blocks.crafting_table, ComponentTypes.FUSE.getItemStack(), Blocks.crafting_table}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.flint_and_steel, ComponentTypes.SIMPLE_PCB.getItemStack()}});
        ModuleData cheatengine = new ModuleData(61, "Creative Engine", ModuleCheatEngine.class, 1);
        ModuleData internalTank = (new ModuleData(63, "Internal Tank", ModuleInternalTank.class, 37)).setAllowDuplicate().addRecipe(new Object[][] {{ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}, {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_VALVE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}, {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}});
        ModuleData sideTank = (new ModuleData(64, "Side Tanks", ModuleSideTanks.class, 10)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).addRecipe(new Object[][] {{ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}, {ComponentTypes.LARGE_TANK_PANE.getItemStack(), ComponentTypes.TANK_VALVE.getItemStack(), ComponentTypes.LARGE_TANK_PANE.getItemStack()}, {ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}});
        ModuleData topTank = (new ModuleData(65, "Top Tank", ModuleTopTank.class, 22)).addSide(ModuleData.SIDE.TOP).addRecipe(new Object[][] {{ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}, {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_VALVE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}, {ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}});
        ModuleData advancedTank = (new ModuleData(66, "Advanced Tank", ModuleAdvancedTank.class, 54)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.TOP, ModuleData.SIDE.CENTER}).addRecipe(new Object[][] {{ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}, {ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.TANK_VALVE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}, {ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}});
        ModuleData frontTank = (new ModuleData(67, "Front Tank", ModuleFrontTank.class, 15)).addSide(ModuleData.SIDE.FRONT).addRecipe(new Object[][] {{ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.LARGE_TANK_PANE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}, {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_VALVE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}, {ComponentTypes.LARGE_TANK_PANE.getItemStack(), ComponentTypes.LARGE_TANK_PANE.getItemStack(), ComponentTypes.LARGE_TANK_PANE.getItemStack()}});
        ModuleData creativeTank = (new ModuleData(72, "Creative Tank", ModuleCheatTank.class, 1)).setAllowDuplicate().addMessage(Localization.MODULE_INFO.OCEAN_MESSAGE);
        ModuleData topTankOpen = (new ModuleData(73, "Open Tank", ModuleOpenTank.class, 31)).addSide(ModuleData.SIDE.TOP).addRecipe(new Object[][] {{ComponentTypes.TANK_PANE.getItemStack(), null, ComponentTypes.TANK_PANE.getItemStack()}, {ComponentTypes.TANK_PANE.getItemStack(), ComponentTypes.TANK_VALVE.getItemStack(), ComponentTypes.TANK_PANE.getItemStack()}, {ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack(), ComponentTypes.HUGE_TANK_PANE.getItemStack()}});
        addNemesis(frontTank, cleaner);
        tankGroup.add(internalTank).add(sideTank).add(topTank).add(advancedTank).add(frontTank).add(creativeTank).add(topTankOpen);
        (new ModuleData(68, "Incinerator", ModuleIncinerator.class, 23)).addRequirement(tankGroup).addRequirement(drillGroup).addRecipe(new Object[][] {{Blocks.nether_brick, Blocks.nether_brick, Blocks.nether_brick}, {Blocks.obsidian, Blocks.furnace, Blocks.obsidian}, {Blocks.nether_brick, Blocks.nether_brick, Blocks.nether_brick}});
        ModuleData thermal0 = (new ModuleData(69, "Thermal Engine", ModuleThermalStandard.class, 28)).addRequirement(tankGroup).addRecipe(new Object[][] {{Blocks.nether_brick, Blocks.nether_brick, Blocks.nether_brick}, {Blocks.obsidian, Blocks.furnace, Blocks.obsidian}, {Blocks.piston, null, Blocks.piston}});
        ModuleData thermal1 = (new ModuleData(70, "Advanced Thermal Engine", ModuleThermalAdvanced.class, 58)).addRequirement(tankGroup.copy(2)).addRecipe(new Object[][] {{Blocks.nether_brick, Blocks.nether_brick, Blocks.nether_brick}, {ComponentTypes.REINFORCED_METAL.getItemStack(), thermal0.getItemStack(), ComponentTypes.REINFORCED_METAL.getItemStack()}, {Blocks.piston, null, Blocks.piston}});
        addNemesis(thermal0, thermal1);
        ModuleData cleanerliquid = (new ModuleData(71, "Liquid Cleaner", ModuleLiquidDrainer.class, 30)).addSide(ModuleData.SIDE.CENTER).addParent(liquidsensors).addRequirement(tankGroup).addRecipe(new Object[][] {{ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(), ComponentTypes.LIQUID_CLEANING_CORE.getItemStack(), ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()}, {ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(), null, ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()}, {ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack(), null, ComponentTypes.LIQUID_CLEANING_TUBE.getItemStack()}});
        addNemesis(frontTank, cleanerliquid);
        addNemesis(frontChest, cleanerliquid);
        ItemStack yellowWool = new ItemStack(Blocks.wool, 1, 4);
        ModuleData eggBasket = (new ModuleData(74, "Egg Basket", ModuleEggBasket.class, 14)).addSide(ModuleData.SIDE.TOP).useExtraData((byte)1).addRecipe(new Object[][] {{yellowWool, yellowWool, yellowWool}, {ComponentTypes.EXPLOSIVE_EASTER_EGG.getItemStack(), ComponentTypes.CHEST_LOCK.getItemStack(), ComponentTypes.BURNING_EASTER_EGG.getItemStack()}, {ComponentTypes.GLISTERING_EASTER_EGG.getItemStack(), ComponentTypes.BASKET.getItemStack(), ComponentTypes.CHOCOLATE_EASTER_EGG.getItemStack()}});

        ModuleDataGroup builderGroup = new ModuleDataGroup(Localization.MODULE_INFO.BUILDER_GROUP);
        builderGroup.add(largebridge);

        ModuleData intelligence = (new ModuleData(75, "Drill Intelligence", ModuleDrillIntelligence.class, 21)).addRequirement(drillGroup).addRecipe(new Object[][] {{Items.gold_ingot, Items.gold_ingot, Items.gold_ingot}, {Items.iron_ingot, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.iron_ingot}, {ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack()}});

        ModuleData builderIntelligence = (new ModuleData(102, "Builder Intelligence", ModuleBuilderIntelligence.class, 21)).addRequirement(builderGroup).addRecipe(new Object[][]{{Items.gold_ingot, Items.gold_ingot, Items.gold_ingot}, {Items.iron_ingot, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.iron_ingot}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.redstone, ComponentTypes.SIMPLE_PCB.getItemStack()}});

        (new ModuleData(77, "Power Observer", ModulePowerObserver.class, 12)).addRequirement(engineGroup).addRecipe(new Object[][] {{null, Blocks.piston, null}, {Items.iron_ingot, Items.redstone, Items.iron_ingot}, {Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone}});
        (new ModuleData(105, "Active Power Observer", ModuleActivePowerObserver.class, 12)).addRequirement(engineGroup).addRecipe(new Object[][] {{null, Blocks.piston, null}, {Items.iron_ingot, ComponentTypes.SIMPLE_PCB, Items.iron_ingot}, {Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone}});
        engineGroup.add(coalTiny);
        engineGroup.add(coalStandard);
        engineGroup.add(solar0);
        engineGroup.add(solar1);
        engineGroup.add(thermal0);
        engineGroup.add(thermal1);
        engineGroup.add(compactsolar);
        engineGroup.add(cheatengine);
        engineGroup.add(rfengine);
        ModuleDataGroup toolGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_GROUP, drillGroup, woodcutterGroup);
        toolGroup.add(farmerGroup);
        ModuleDataGroup enchantableGroup = ModuleDataGroup.getCombinedGroup(Localization.MODULE_INFO.TOOL_OR_SHOOTER_GROUP, toolGroup, shooterGroup);
        (new ModuleData(82, "Enchanter", ModuleEnchants.class, 72)).addRequirement(enchantableGroup).addRecipe(new Object[][] {{null, ComponentTypes.GALGADORIAN_METAL.getItemStack(), null}, {Items.book, Blocks.enchanting_table, Items.book}, {Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone}});
        (new ModuleData(83, "Ore Extractor", ModuleOreTracker.class, 80)).addRequirement(drillGroup).addRecipe(new Object[][] {{Blocks.redstone_torch, null, Blocks.redstone_torch}, {ComponentTypes.EYE_OF_GALGADOR.getItemStack(), ComponentTypes.GALGADORIAN_METAL.getItemStack(), ComponentTypes.EYE_OF_GALGADOR.getItemStack()}, {Items.quartz, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.quartz}});
        ModuleData flowerremover = (new ModuleData(85, "Lawn Mower", ModuleFlowerRemover.class, 38)).addSides(new ModuleData.SIDE[] {ModuleData.SIDE.RIGHT, ModuleData.SIDE.LEFT}).addRecipe(new Object[][] {{ComponentTypes.BLADE_ARM.getItemStack(), null, ComponentTypes.BLADE_ARM.getItemStack()}, {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}, {ComponentTypes.BLADE_ARM.getItemStack(), null, ComponentTypes.BLADE_ARM.getItemStack()}});
        (new ModuleData(86, "Milker", ModuleMilker.class, 26)).addParent(cage).addRecipe(new Object[][] {{Items.wheat, Items.wheat, Items.wheat}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.bucket, ComponentTypes.SIMPLE_PCB.getItemStack()}, {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}});
        ModuleData crafter = (new ModuleData(87, "Crafter", ModuleCrafter.class, 22)).setAllowDuplicate().addRecipe(new Object[][] {{ComponentTypes.SIMPLE_PCB.getItemStack()}, {Blocks.crafting_table}});
        (new ModuleData(88, "Tree: Exotic", ModuleModTrees.class, 30)).addRequirement(woodcutterGroup).addRecipe(new Object[][] {{Items.glowstone_dust, null, Items.glowstone_dust}, {Items.redstone, Blocks.sapling, Items.redstone}, {ComponentTypes.SIMPLE_PCB.getItemStack(), ComponentTypes.EMPTY_DISK.getItemStack(), ComponentTypes.SIMPLE_PCB.getItemStack()}});
        (new ModuleData(106, "Tree: Large", ModuleLargeTrees.class, 30)).addRequirement(woodcutterGroup).addRecipe(new Object[][] {{Items.cookie, null, Items.cookie}, {Items.redstone, Blocks.sapling, Items.redstone}, {ComponentTypes.SIMPLE_PCB.getItemStack(), ComponentTypes.EMPTY_DISK.getItemStack(), ComponentTypes.SIMPLE_PCB.getItemStack()}});
        (new ModuleData(89, "Planter Range Extender", ModulePlantSize.class, 20)).addRequirement(woodcutterGroup).addRecipe(new Object[][] {{Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone}, {null, Blocks.sapling, null}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.sapling, ComponentTypes.SIMPLE_PCB.getItemStack()}});
        (new ModuleData(78, "Steve\'s Arcade", ModuleArcade.class, 10)).addParent(seat).addRecipe(new Object[][] {{null, Blocks.glass_pane, null}, {planks, ComponentTypes.SIMPLE_PCB.getItemStack(), planks}, {Items.redstone, planks, Items.redstone}});
        ModuleData smelter = (new ModuleData(91, "Smelter", ModuleSmelter.class, 22)).setAllowDuplicate().addRecipe(new Object[][] {{ComponentTypes.SIMPLE_PCB.getItemStack()}, {Blocks.furnace}});
        (new ModuleData(92, "Advanced Crafter", ModuleCrafterAdv.class, 42)).setAllowDuplicate().addRecipe(new Object[][] {{null, Items.diamond, null}, {null, ComponentTypes.ADVANCED_PCB.getItemStack(), null}, {ComponentTypes.SIMPLE_PCB.getItemStack(), crafter.getItemStack(), ComponentTypes.SIMPLE_PCB.getItemStack()}});
        (new ModuleData(93, "Advanced Smelter", ModuleSmelterAdv.class, 42)).setAllowDuplicate().addRecipe(new Object[][] {{null, Items.diamond, null}, {null, ComponentTypes.ADVANCED_PCB.getItemStack(), null}, {ComponentTypes.SIMPLE_PCB.getItemStack(), smelter.getItemStack(), ComponentTypes.SIMPLE_PCB.getItemStack()}});
        (new ModuleData(94, "Information Provider", ModuleLabel.class, 12)).addRecipe(new Object[][] {{Blocks.glass_pane, Blocks.glass_pane, Blocks.glass_pane}, {Items.iron_ingot, Items.glowstone_dust, Items.iron_ingot}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.sign, ComponentTypes.SIMPLE_PCB.getItemStack()}});
        (new ModuleData(95, "Experience Bank", ModuleExperience.class, 36)).addRecipe(new Object[][] {{null, Items.redstone, null}, {Items.glowstone_dust, Items.emerald, Items.glowstone_dust}, {ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.cauldron, ComponentTypes.SIMPLE_PCB.getItemStack()}});
        (new ModuleData(96, "Creative Incinerator", ModuleCreativeIncinerator.class, 1)).addRequirement(drillGroup);
        new ModuleData(97, "Creative Supplies", ModuleCreativeSupplies.class, 1);
        (new ModuleData(99, "Cake Server", ModuleCakeServer.class, 10)).addSide(ModuleData.SIDE.TOP).addMessage(Localization.MODULE_INFO.ALPHA_MESSAGE).addRecipe(new Object[][] {{null, Items.cake, null}, {"slabWood", "slabWood", "slabWood"}, {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}});
        ModuleData trickOrTreat = (new ModuleData(100, "Trick-or-Treat Cake Server", ModuleCakeServerDynamite.class, 15)).addSide(ModuleData.SIDE.TOP).addRecipe(new Object[][] {{null, Items.cake, null}, {"slabWood", "slabWood", "slabWood"}, {ComponentTypes.DYNAMITE.getItemStack(), ComponentTypes.SIMPLE_PCB.getItemStack(), ComponentTypes.DYNAMITE.getItemStack()}});

        if (!StevesCarts.isHalloween)
        {
            bats.lock();
            pumpkinhull.lock();
            trickOrTreat.lock();
        }
        if (!StevesCarts.isChristmas) {
            gift.lock();
            snowgenerator.lock();
            snowballshooter.lock();
        }
        if (!StevesCarts.isEaster)
        {
            eggBasket.lock();
        }

        // TODO Make the Active Power Observer able to disable modules
        // TODO Make the hydrator/fertilizer/etc not stop the cart on failure
    }

    @SideOnly(Side.CLIENT)
    public static void initModels()
    {
        ((ModuleData)moduleList.get(Byte.valueOf((byte)0))).addModel("Engine", new ModelEngineFrame()).addModel("Fire", new ModelEngineInside());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)44))).addModel("Engine", new ModelEngineFrame()).addModel("Fire", new ModelEngineInside());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)1))).addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(4));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)45))).addModel("SolarPanelBase", new ModelSolarPanelBase()).addModel("SolarPanels", new ModelSolarPanelHeads(2));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)56))).addModel("SolarPanelSide", new ModelCompactSolarPanel());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)2))).addModel("SideChest", new ModelSideChests());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)3))).removeModel("Top").addModel("TopChest", new ModelTopChest());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)4))).addModel("FrontChest", new ModelFrontChest()).setModelMult(0.68F);
        ((ModuleData)moduleList.get(Byte.valueOf((byte)6))).addModel("SideChest", new ModelExtractingChests());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)7))).addModel("Torch", new ModelTorchplacer());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)8))).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)42))).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelIron.png"))).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)43))).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelHardened.png"))).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)9))).addModel("Drill", new ModelDrill(ResourceHelper.getResource("/models/drillModelMagic.png"))).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)10))).addModel("Rails", new ModelRailer(3));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)11))).addModel("Rails", new ModelRailer(6));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)12))).addModel("Bridge", new ModelBridge()).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)17))).addModel("LargeBridge", new ModelBridge()).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)13))).addModel("Remover", new ModelTrackRemover()).setModelMult(0.6F);
        ((ModuleData)moduleList.get(Byte.valueOf((byte)14))).addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelDiamond.png"))).setModelMult(0.45F);
        ((ModuleData)moduleList.get(Byte.valueOf((byte)84))).addModel("Farmer", new ModelFarmer(ResourceHelper.getResource("/models/farmerModelGalgadorian.png"))).setModelMult(0.45F);
        ((ModuleData)moduleList.get(Byte.valueOf((byte)15))).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelDiamond.png"))).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)79))).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelHardened.png"))).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)80))).addModel("WoodCutter", new ModelWoodCutter(ResourceHelper.getResource("/models/woodCutterModelGalgadorian.png"))).addModel("Plate", new ModelToolPlate());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)20))).addModel("Sensor", new ModelLiquidSensors());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)25))).removeModel("Top").addModel("Chair", new ModelSeat());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)26))).addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel.png")));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)27))).addModel("Lever", new ModelLever(ResourceHelper.getResource("/models/leverModel2.png"))).addModel("Wheel", new ModelWheel());
        ArrayList pipes = new ArrayList();

        for (int i = 0; i < 9; ++i)
        {
            if (i != 4)
            {
                pipes.add(Integer.valueOf(i));
            }
        }

        ((ModuleData)moduleList.get(Byte.valueOf((byte)28))).addModel("Rig", new ModelShootingRig()).addModel("Pipes", new ModelGun(pipes));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)29))).addModel("Rig", new ModelShootingRig()).addModel("MobDetector", new ModelMobDetector()).addModel("Pipes", new ModelSniperRifle());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)30))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"), false)).addModel("Cleaner", new ModelCleaner());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)31))).addModel("Tnt", new ModelDynamite());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)32))).addModel("Shield", new ModelShield()).setModelMult(0.68F);
        ((ModuleData)moduleList.get(Byte.valueOf((byte)37))).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelWooden.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)38))).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelStandard.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelStandardTop.png")));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)39))).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelLarge.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelLargeTop.png")));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)47))).addModel("Hull", new ModelPumpkinHull(ResourceHelper.getResource("/models/hullModelPumpkin.png"), ResourceHelper.getResource("/models/hullModelWooden.png"))).addModel("Top", new ModelPumpkinHullTop(ResourceHelper.getResource("/models/hullModelPumpkinTop.png"), ResourceHelper.getResource("/models/hullModelWoodenTop.png")));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)62))).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelPig.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelPigTop.png"))).addModel("Head", new ModelPigHead()).addModel("Tail", new ModelPigTail()).addModel("Helmet", new ModelPigHelmet(false)).addModel("Helmet_Overlay", new ModelPigHelmet(true));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)76))).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelCreative.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelCreativeTop.png")));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)81))).addModel("Hull", new ModelHull(ResourceHelper.getResource("/models/hullModelGalgadorian.png"))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/hullModelGalgadorianTop.png")));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)40))).setModelMult(0.65F).addModel("Speakers", new ModelNote());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)50))).addModel("GiftStorage", new ModelGiftStorage());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)57))).removeModel("Top").addModel("Cage", new ModelCage(false), false).addModel("Cage", new ModelCage(true), true).setModelMult(0.65F);
        ((ModuleData)moduleList.get(Byte.valueOf((byte)64))).addModel("SideTanks", new ModelSideTanks());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)65))).addModel("TopTank", new ModelTopTank(false));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)66))).addModel("LargeTank", new ModelAdvancedTank()).removeModel("Top");
        ((ModuleData)moduleList.get(Byte.valueOf((byte)67))).setModelMult(0.68F).addModel("FrontTank", new ModelFrontTank());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)73))).addModel("TopTank", new ModelTopTank(true));
        ((ModuleData)moduleList.get(Byte.valueOf((byte)71))).addModel("Top", new ModelHullTop(ResourceHelper.getResource("/models/cleanerModelTop.png"), false)).addModel("Cleaner", new ModelLiquidDrainer());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)74))).addModel("TopChest", new ModelEggBasket());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)85))).addModel("LawnMower", new ModelLawnMower()).setModelMult(0.4F);
        ((ModuleData)moduleList.get(Byte.valueOf((byte)99))).addModel("Cake", new ModelCake());
        ((ModuleData)moduleList.get(Byte.valueOf((byte)100))).addModel("Cake", new ModelCake());
    }

    public ModuleData(int id, String name, Class <? extends ModuleBase > moduleClass, int modularCost)
    {
        this.id = (byte)id;
        this.moduleClass = moduleClass;
        this.name = name;
        this.modularCost = modularCost;
        this.groupID = moduleGroups.length;

        for (int i = 0; i < moduleGroups.length; ++i)
        {
            if (moduleGroups[i].isAssignableFrom(moduleClass))
            {
                this.groupID = i;
                break;
            }
        }

        if (moduleList.containsKey(Byte.valueOf(this.id)))
        {
            System.out.println("WARNING! " + name + " can\'t be added with ID " + id + " since that ID is already occupied by " + ((ModuleData)moduleList.get(Byte.valueOf(this.id))).getName());
        }
        else
        {
            moduleList.put(Byte.valueOf(this.id), this);
            StevesCarts.logger.info("" + this.id + " -> " + this.name);
        }
    }

    public Class <? extends ModuleBase > getModuleClass()
    {
        return this.moduleClass;
    }

    public boolean getIsValid()
    {
        return this.isValid;
    }

    public boolean getIsLocked()
    {
        return this.isLocked;
    }

    protected ModuleData lock()
    {
        this.isLocked = true;
        return this;
    }

    public boolean getEnabledByDefault()
    {
        return !this.defaultLock;
    }

    protected ModuleData lockByDefault()
    {
        this.defaultLock = true;
        return this;
    }

    protected ModuleData setAllowDuplicate()
    {
        this.allowDuplicate = true;
        return this;
    }

    protected boolean getAllowDuplicate()
    {
        return this.allowDuplicate;
    }

    protected ModuleData addSide(ModuleData.SIDE side)
    {
        if (this.renderingSides == null)
        {
            this.renderingSides = new ArrayList();
        }

        this.renderingSides.add(side);

        if (side == ModuleData.SIDE.TOP)
        {
            this.removeModel("Rails");
        }

        return this;
    }

    public ModuleData useExtraData(byte defaultValue)
    {
        this.extraDataDefaultValue = defaultValue;
        this.useExtraData = true;
        return this;
    }

    public boolean isUsingExtraData()
    {
        return this.useExtraData;
    }

    public byte getDefaultExtraData()
    {
        return this.extraDataDefaultValue;
    }

    public ArrayList<ModuleData.SIDE> getRenderingSides()
    {
        return this.renderingSides;
    }

    protected ModuleData addSides(ModuleData.SIDE[] sides)
    {
        for (int i = 0; i < sides.length; ++i)
        {
            this.addSide(sides[i]);
        }

        return this;
    }

    protected ModuleData addParent(ModuleData parent)
    {
        this.parent = parent;
        return this;
    }

    protected ModuleData addMessage(Localization.MODULE_INFO s)
    {
        if (this.message == null)
        {
            this.message = new ArrayList();
        }

        this.message.add(s);
        return this;
    }

    protected void addNemesis(ModuleData nemesis)
    {
        if (this.nemesis == null)
        {
            this.nemesis = new ArrayList();
        }

        this.nemesis.add(nemesis);
    }

    protected ModuleData addRequirement(ModuleDataGroup requirement)
    {
        if (this.requirement == null)
        {
            this.requirement = new ArrayList();
        }

        this.requirement.add(requirement);
        return this;
    }

    protected static void addNemesis(ModuleData m1, ModuleData m2)
    {
        m2.addNemesis(m1);
        m1.addNemesis(m2);
    }

    public float getModelMult()
    {
        return this.modelMult;
    }

    protected ModuleData setModelMult(float val)
    {
        this.modelMult = val;
        return this;
    }

    protected ModuleData addModel(String tag, ModelCartbase model)
    {
        this.addModel(tag, model, false);
        this.addModel(tag, model, true);
        return this;
    }

    protected ModuleData addModel(String tag, ModelCartbase model, boolean placeholder)
    {
        if (placeholder)
        {
            if (this.modelsPlaceholder == null)
            {
                this.modelsPlaceholder = new HashMap();
            }

            this.modelsPlaceholder.put(tag, model);
        }
        else
        {
            if (this.models == null)
            {
                this.models = new HashMap();
            }

            this.models.put(tag, model);
        }

        return this;
    }

    public HashMap<String, ModelCartbase> getModels(boolean placeholder)
    {
        return placeholder ? this.modelsPlaceholder : this.models;
    }

    public boolean haveModels(boolean placeholder)
    {
        return placeholder ? this.modelsPlaceholder != null : this.models != null;
    }

    protected ModuleData removeModel(String tag)
    {
        if (this.removedModels == null)
        {
            this.removedModels = new ArrayList();
        }

        if (!this.removedModels.contains(tag))
        {
            this.removedModels.add(tag);
        }

        return this;
    }

    public ArrayList<String> getRemovedModels()
    {
        return this.removedModels;
    }

    public boolean haveRemovedModels()
    {
        return this.removedModels != null;
    }

    public String getName()
    {
        return StatCollector.translateToLocal(this.getUnlocalizedName());
    }

    public String getUnlocalizedName()
    {
        return "item.SC2:" + this.getRawName() + ".name";
    }

    public byte getID()
    {
        return this.id;
    }

    public int getCost()
    {
        return this.modularCost;
    }

    protected ModuleData getParent()
    {
        return this.parent;
    }

    protected ArrayList<ModuleData> getNemesis()
    {
        return this.nemesis;
    }

    protected ArrayList<ModuleDataGroup> getRequirement()
    {
        return this.requirement;
    }

    public boolean getHasRecipe()
    {
        return this.hasRecipe;
    }

    public String getModuleInfoText(byte b)
    {
        return null;
    }

    public String getCartInfoText(String name, byte b)
    {
        return name;
    }

    public static ArrayList<ItemStack> getModularItems(ItemStack cart)
    {
        ArrayList modules = new ArrayList();

        if (cart != null && cart.getItem() == ModItems.carts && cart.getTagCompound() != null)
        {
            NBTTagCompound info = cart.getTagCompound();

            if (info.hasKey("Modules"))
            {
                byte[] IDs = info.getByteArray("Modules");

                for (int i = 0; i < IDs.length; ++i)
                {
                    byte id = IDs[i];
                    ItemStack module = new ItemStack(ModItems.modules, 1, id);
                    ModItems.modules.addExtraDataToModule(module, info, i);
                    modules.add(module);
                }
            }
        }

        return modules;
    }

    public static ItemStack createModularCart(MinecartModular parentcart)
    {
        ItemStack cart = new ItemStack(ModItems.carts, 1);
        NBTTagCompound save = new NBTTagCompound();
        byte[] moduleIDs = new byte[parentcart.getModules().size()];
        int i = 0;

        while (i < parentcart.getModules().size())
        {
            ModuleBase module = (ModuleBase)parentcart.getModules().get(i);
            Iterator i$ = moduleList.values().iterator();

            while (true)
            {
                if (i$.hasNext())
                {
                    ModuleData moduledata = (ModuleData)i$.next();

                    if (module.getClass() != moduledata.moduleClass)
                    {
                        continue;
                    }

                    moduleIDs[i] = moduledata.getID();
                }

                ModItems.modules.addExtraDataToModule(save, module, i);
                ++i;
                break;
            }
        }

        save.setByteArray("Modules", moduleIDs);
        cart.setTagCompound(save);
        CartVersion.addVersion(cart);
        return cart;
    }

    public static ItemStack createModularCartFromItems(ArrayList<ItemStack> modules)
    {
        ItemStack cart = new ItemStack(ModItems.carts, 1);
        NBTTagCompound save = new NBTTagCompound();
        byte[] moduleIDs = new byte[modules.size()];

        for (int i = 0; i < moduleIDs.length; ++i)
        {
            moduleIDs[i] = (byte)((ItemStack)modules.get(i)).getItemDamage();
            ModItems.modules.addExtraDataToCart(save, (ItemStack)modules.get(i), i);
        }

        save.setByteArray("Modules", moduleIDs);
        cart.setTagCompound(save);
        CartVersion.addVersion(cart);
        return cart;
    }

    public static boolean isItemOfModularType(ItemStack itemstack, Class <? extends ModuleBase > validClass)
    {
        if (itemstack.getItem() == ModItems.modules)
        {
            ModuleData module = ModItems.modules.getModuleData(itemstack);

            if (module != null && validClass.isAssignableFrom(module.moduleClass))
            {
                return true;
            }
        }

        return false;
    }

    protected ModuleData addRecipe(Object[][] recipe)
    {
        if (this.recipes == null)
        {
            this.recipes = new ArrayList();
        }

        this.recipes.add(recipe);
        return this;
    }

    public void loadRecipe()
    {
        if (!this.isLocked)
        {
            this.isValid = true;

            if (this.recipes != null)
            {
                this.hasRecipe = true;
                Iterator i$ = this.recipes.iterator();

                while (i$.hasNext())
                {
                    Object[][] recipe = (Object[][])i$.next();
                    RecipeHelper.addRecipe(this.getItemStack(), recipe);
                }
            }
        }
    }

    public ItemStack getItemStack()
    {
        ItemStack module = new ItemStack(ModItems.modules, 1, this.id);

        if (this.isUsingExtraData())
        {
            NBTTagCompound save = new NBTTagCompound();
            save.setByte("Data", this.getDefaultExtraData());
            module.setTagCompound(save);
        }

        return module;
    }

    public static boolean isValidModuleItem(int validGroup, ItemStack itemstack)
    {
        if (itemstack.getItem() == ModItems.modules)
        {
            ModuleData module = ModItems.modules.getModuleData(itemstack);
            return isValidModuleItem(validGroup, module);
        }
        else
        {
            return false;
        }
    }

    public static boolean isValidModuleItem(int validGroup, ModuleData module)
    {
        if (module != null)
        {
            if (validGroup < 0)
            {
                for (int i = 0; i < moduleGroups.length; ++i)
                {
                    if (moduleGroups[i].isAssignableFrom(module.moduleClass))
                    {
                        return false;
                    }
                }

                return true;
            }

            if (moduleGroups[validGroup].isAssignableFrom(module.moduleClass))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean isValidModuleCombo(ModuleDataHull hull, ArrayList<ModuleData> modules)
    {
        int[] max = new int[] {1, hull.getEngineMax(), 1, 4, hull.getAddonMax(), 6};
        int[] current = new int[max.length];
        Iterator i$ = modules.iterator();

        while (i$.hasNext())
        {
            ModuleData module = (ModuleData)i$.next();
            int id = 5;
            int i = 0;

            while (true)
            {
                if (i < 5)
                {
                    if (!isValidModuleItem(i, module))
                    {
                        ++i;
                        continue;
                    }

                    id = i;
                }

                ++current[id];

                if (current[id] > max[id])
                {
                    return false;
                }

                break;
            }
        }

        return true;
    }

    public void addExtraMessage(List list)
    {
        if (this.message != null)
        {
            list.add("");
            Iterator i$ = this.message.iterator();

            while (i$.hasNext())
            {
                Localization.MODULE_INFO m = (Localization.MODULE_INFO)i$.next();
                String str = m.translate(new String[0]);

                if (str.length() <= 30)
                {
                    this.addExtraMessage(list, str);
                }
                else
                {
                    String[] words = str.split(" ");
                    String row = "";
                    String[] arr$ = words;
                    int len$ = words.length;

                    for (int i$1 = 0; i$1 < len$; ++i$1)
                    {
                        String word = arr$[i$1];
                        String next = (row + " " + word).trim();

                        if (next.length() <= 30)
                        {
                            row = next;
                        }
                        else
                        {
                            this.addExtraMessage(list, row);
                            row = word;
                        }
                    }

                    this.addExtraMessage(list, row);
                }
            }
        }
    }

    private void addExtraMessage(List list, String str)
    {
        list.add(ColorHelper.GRAY + "\u00a7o" + str + "\u00a7r");
    }

    public void addSpecificInformation(List list)
    {
        list.add(ColorHelper.LIGHTGRAY + Localization.MODULE_INFO.MODULAR_COST.translate(new String[0]) + ": " + this.modularCost);
    }

    public final void addInformation(List list, NBTTagCompound compound)
    {
        this.addSpecificInformation(list);
        String i$;

        if (compound != null && compound.hasKey("Data"))
        {
            i$ = this.getModuleInfoText(compound.getByte("Data"));

            if (i$ != null)
            {
                list.add(ColorHelper.WHITE + i$);
            }
        }

        if (GuiScreen.isShiftKeyDown())
        {
            if (this.getRenderingSides() != null && this.getRenderingSides().size() != 0)
            {
                i$ = "";

                for (int group = 0; group < this.getRenderingSides().size(); ++group)
                {
                    ModuleData.SIDE side = (ModuleData.SIDE)this.getRenderingSides().get(group);

                    if (group == 0)
                    {
                        i$ = i$ + side.toString();
                    }
                    else if (group == this.getRenderingSides().size() - 1)
                    {
                        i$ = i$ + " " + Localization.MODULE_INFO.AND.translate(new String[0]) + " " + side.toString();
                    }
                    else
                    {
                        i$ = i$ + ", " + side.toString();
                    }
                }

                list.add(ColorHelper.CYAN + Localization.MODULE_INFO.OCCUPIED_SIDES.translate(new String[] {i$, String.valueOf(this.getRenderingSides().size())}));
            }
            else
            {
                list.add(ColorHelper.CYAN + Localization.MODULE_INFO.NO_SIDES.translate(new String[0]));
            }

            Iterator var6;

            if (this.getNemesis() != null && this.getNemesis().size() != 0)
            {
                if (this.getRenderingSides() != null && this.getRenderingSides().size() != 0)
                {
                    list.add(ColorHelper.RED + Localization.MODULE_INFO.CONFLICT_ALSO.translate(new String[0]) + ":");
                }
                else
                {
                    list.add(ColorHelper.RED + Localization.MODULE_INFO.CONFLICT_HOWEVER.translate(new String[0]) + ":");
                }

                var6 = this.getNemesis().iterator();

                while (var6.hasNext())
                {
                    ModuleData var7 = (ModuleData)var6.next();
                    list.add(ColorHelper.RED + var7.getName());
                }
            }

            if (this.parent != null)
            {
                list.add(ColorHelper.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate(new String[0]) + " " + this.parent.getName());
            }

            if (this.getRequirement() != null && this.getRequirement().size() != 0)
            {
                var6 = this.getRequirement().iterator();

                while (var6.hasNext())
                {
                    ModuleDataGroup var8 = (ModuleDataGroup)var6.next();
                    list.add(ColorHelper.YELLOW + Localization.MODULE_INFO.REQUIREMENT.translate(new String[0]) + " " + var8.getCountName() + " " + var8.getName());
                }
            }

            if (this.getAllowDuplicate())
            {
                list.add(ColorHelper.LIME + Localization.MODULE_INFO.DUPLICATES.translate(new String[0]));
            }
        }

        list.add(ColorHelper.LIGHTBLUE + Localization.MODULE_INFO.TYPE.translate(new String[0]) + ": " + moduleGroupNames[this.groupID].translate(new String[0]));
        this.addExtraMessage(list);
    }

    public static String checkForErrors(ModuleDataHull hull, ArrayList<ModuleData> modules)
    {
        if (getTotalCost(modules) > hull.getCapacity())
        {
            return Localization.MODULE_INFO.CAPACITY_ERROR.translate(new String[0]);
        }
        else if (!isValidModuleCombo(hull, modules))
        {
            return Localization.MODULE_INFO.COMBINATION_ERROR.translate(new String[0]);
        }
        else
        {
            for (int i = 0; i < modules.size(); ++i)
            {
                ModuleData mod1 = (ModuleData)modules.get(i);

                if (mod1.getCost() > hull.getComplexityMax())
                {
                    return Localization.MODULE_INFO.COMPLEXITY_ERROR.translate(new String[] {mod1.getName()});
                }

                if (mod1.getParent() != null && !modules.contains(mod1.getParent()))
                {
                    return Localization.MODULE_INFO.PARENT_ERROR.translate(new String[] {mod1.getName(), mod1.getParent().getName()});
                }

                Iterator j;
                ModuleData mod2;

                if (mod1.getNemesis() != null)
                {
                    j = mod1.getNemesis().iterator();

                    while (j.hasNext())
                    {
                        mod2 = (ModuleData)j.next();

                        if (modules.contains(mod2))
                        {
                            return Localization.MODULE_INFO.NEMESIS_ERROR.translate(new String[] {mod1.getName(), mod2.getName()});
                        }
                    }
                }

                Iterator i$;
                Iterator i$1;

                if (mod1.getRequirement() != null)
                {
                    j = mod1.getRequirement().iterator();

                    while (j.hasNext())
                    {
                        ModuleDataGroup var12 = (ModuleDataGroup)j.next();
                        int clash = 0;
                        i$ = var12.getModules().iterator();

                        while (i$.hasNext())
                        {
                            ModuleData side1 = (ModuleData)i$.next();
                            i$1 = modules.iterator();

                            while (i$1.hasNext())
                            {
                                ModuleData side2 = (ModuleData)i$1.next();

                                if (side1.equals(side2))
                                {
                                    ++clash;
                                }
                            }
                        }

                        if (clash < var12.getCount())
                        {
                            return Localization.MODULE_INFO.PARENT_ERROR.translate(new String[] {mod1.getName(), var12.getCountName() + " " + var12.getName()});
                        }
                    }
                }

                for (int var11 = i + 1; var11 < modules.size(); ++var11)
                {
                    mod2 = (ModuleData)modules.get(var11);

                    if (mod1 == mod2)
                    {
                        if (!mod1.getAllowDuplicate())
                        {
                            return Localization.MODULE_INFO.DUPLICATE_ERROR.translate(new String[] {mod1.getName()});
                        }
                    }
                    else if (mod1.getRenderingSides() != null && mod2.getRenderingSides() != null)
                    {
                        ModuleData.SIDE var13 = ModuleData.SIDE.NONE;
                        i$ = mod1.getRenderingSides().iterator();

                        while (i$.hasNext())
                        {
                            ModuleData.SIDE var14 = (ModuleData.SIDE)i$.next();
                            i$1 = mod2.getRenderingSides().iterator();

                            while (i$1.hasNext())
                            {
                                ModuleData.SIDE var15 = (ModuleData.SIDE)i$1.next();

                                if (var14 == var15)
                                {
                                    var13 = var14;
                                    break;
                                }
                            }

                            if (var13 != ModuleData.SIDE.NONE)
                            {
                                break;
                            }
                        }

                        if (var13 != ModuleData.SIDE.NONE)
                        {
                            return Localization.MODULE_INFO.CLASH_ERROR.translate(new String[] {mod1.getName(), mod2.getName(), var13.toString()});
                        }
                    }
                }
            }

            return null;
        }
    }

    public static int getTotalCost(ArrayList<ModuleData> modules)
    {
        int currentCost = 0;
        ModuleData module;

        for (Iterator i$ = modules.iterator(); i$.hasNext(); currentCost += module.getCost())
        {
            module = (ModuleData)i$.next();
        }

        return currentCost;
    }

    private static long calculateCombinations()
    {
        long combinations = 0L;
        ArrayList potential = new ArrayList();
        Iterator i$ = moduleList.values().iterator();
        ModuleData module;

        while (i$.hasNext())
        {
            module = (ModuleData)i$.next();

            if (!(module instanceof ModuleDataHull))
            {
                potential.add(module);
            }
        }

        i$ = moduleList.values().iterator();

        while (i$.hasNext())
        {
            module = (ModuleData)i$.next();

            if (module instanceof ModuleDataHull)
            {
                ArrayList modules = new ArrayList();
                combinations += populateHull((ModuleDataHull)module, modules, (ArrayList)potential.clone(), 0);
                System.out.println("Hull added: " + combinations);
            }
        }

        return combinations;
    }

    private static long populateHull(ModuleDataHull hull, ArrayList<ModuleData> attached, ArrayList<ModuleData> potential, int depth)
    {
        if (checkForErrors(hull, attached) != null)
        {
            return 0L;
        }
        else
        {
            long combinations = 1L;
            Iterator itt = potential.iterator();

            while (itt.hasNext())
            {
                ModuleData module = (ModuleData)itt.next();
                ArrayList attachedCopy = (ArrayList)attached.clone();
                attachedCopy.add(module);
                ArrayList potentialCopy = (ArrayList)potential.clone();
                itt.remove();
                combinations += populateHull(hull, attachedCopy, potentialCopy, depth + 1);

                if (depth < 3)
                {
                    System.out.println("Modular state[" + depth + "]: " + combinations);
                }
            }

            return combinations;
        }
    }

    public String getRawName()
    {
        return this.name.replace(":", "").replace("\'", "").replace(" ", "_").replace("-", "_").toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    public void createIcon(IIconRegister register)
    {
        StringBuilder var10002 = new StringBuilder();
        StevesCarts.instance.getClass();
        this.icon = register.registerIcon(var10002.append("stevescarts").append(":").append(this.getRawName()).append("_icon").toString());
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon()
    {
        return this.icon;
    }

    public static enum SIDE
    {
        NONE("NONE", 0, Localization.MODULE_INFO.SIDE_NONE),
        TOP("TOP", 1, Localization.MODULE_INFO.SIDE_TOP),
        CENTER("CENTER", 2, Localization.MODULE_INFO.SIDE_CENTER),
        BOTTOM("BOTTOM", 3, Localization.MODULE_INFO.SIDE_BOTTOM),
        BACK("BACK", 4, Localization.MODULE_INFO.SIDE_BACK),
        LEFT("LEFT", 5, Localization.MODULE_INFO.SIDE_LEFT),
        RIGHT("RIGHT", 6, Localization.MODULE_INFO.SIDE_RIGHT),
        FRONT("FRONT", 7, Localization.MODULE_INFO.SIDE_FRONT);
        private Localization.MODULE_INFO name;

        private static final ModuleData.SIDE[] $VALUES = new ModuleData.SIDE[]{NONE, TOP, CENTER, BOTTOM, BACK, LEFT, RIGHT, FRONT};

        private SIDE(String var1, int var2, Localization.MODULE_INFO name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name.translate(new String[0]);
        }
    }
}
