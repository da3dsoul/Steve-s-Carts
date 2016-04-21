package vswe.stevescarts.Helpers;

import java.util.ArrayList;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Modules.Addons.ModuleBrake;
import vswe.stevescarts.Modules.Addons.ModuleInvisible;
import vswe.stevescarts.Modules.Addons.ModuleLiquidSensors;
import vswe.stevescarts.Modules.Addons.ModuleShield;
import vswe.stevescarts.Modules.Engines.ModuleSolarBase;
import vswe.stevescarts.Modules.Realtimers.ModuleDynamite;
import vswe.stevescarts.Modules.Realtimers.ModuleShooter;
import vswe.stevescarts.Modules.Realtimers.ModuleShooterAdv;
import vswe.stevescarts.Modules.Storages.Chests.ModuleChest;
import vswe.stevescarts.Modules.Storages.Chests.ModuleInternalStorage;
import vswe.stevescarts.Modules.Workers.ModuleBridge;
import vswe.stevescarts.Modules.Workers.ModuleHydrater;
import vswe.stevescarts.Modules.Workers.ModuleRailer;
import vswe.stevescarts.Modules.Workers.ModuleTorch;
import vswe.stevescarts.Modules.Workers.Tools.ModuleDrill;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;
import vswe.stevescarts.Modules.Workers.Tools.ModuleWoodcutter;

public class SimulationInfo
{
    private ArrayList<DropDownMenuItem> items = new ArrayList();
    private DropDownMenuItem itemBOOLChest;
    private DropDownMenuItem itemBOOLInvis;
    private DropDownMenuItem itemBOOLBrake;
    private DropDownMenuItem itemBOOLDrill;
    private DropDownMenuItem itemBOOLLight;
    private DropDownMenuItem itemBOOLBridge;
    private DropDownMenuItem itemBOOLFarm;
    private DropDownMenuItem itemBOOLCut;
    private DropDownMenuItem itemBOOLExplode;
    private DropDownMenuItem itemBOOLShield;
    private DropDownMenuItem itemINTLiquid;
    private DropDownMenuItem itemINTWater;
    private DropDownMenuItem itemINTFuse;
    private DropDownMenuItem itemINTRail;
    private DropDownMenuItem itemINTExplosion;
    private DropDownMenuItem itemMULTIBOOLTorch;
    private DropDownMenuItem itemMULTIBOOLPipes1;
    private DropDownMenuItem itemMULTIBOOLPipes2;
    private DropDownMenuItem itemBOOLPipe;
    private DropDownMenuItem itemINTBackground;
    public int fuse;

    public boolean getShieldActive()
    {
        return this.itemBOOLShield.getBOOL();
    }

    public boolean getChestActive()
    {
        return this.itemBOOLChest.getBOOL();
    }

    public boolean getInvisActive()
    {
        return this.itemBOOLInvis.getBOOL();
    }

    public boolean getBrakeActive()
    {
        return this.itemBOOLBrake.getBOOL();
    }

    public boolean getDrillSpinning()
    {
        return this.itemBOOLDrill.getBOOL();
    }

    public boolean getMaxLight()
    {
        return this.itemBOOLLight.getBOOL();
    }

    public boolean getNeedBridge()
    {
        return this.itemBOOLBridge.getBOOL();
    }

    public boolean getIsFarming()
    {
        return this.itemBOOLFarm.getBOOL();
    }

    public boolean getIsCutting()
    {
        return this.itemBOOLCut.getBOOL();
    }

    public boolean getIsPipeActive()
    {
        return this.itemBOOLPipe.getBOOL();
    }

    public boolean getShouldExplode()
    {
        return this.itemBOOLExplode.getBOOL();
    }

    public int getLiquidLight()
    {
        return this.itemINTLiquid.getINT();
    }

    public int getFuseLength()
    {
        return this.itemINTFuse.getINT() * 2;
    }

    public int getWaterLevel()
    {
        return this.itemINTWater.getINT();
    }

    public int getRailCount()
    {
        return this.itemINTRail.getINT();
    }

    public byte getTorchInfo()
    {
        return this.itemMULTIBOOLTorch.getMULTIBOOL();
    }

    public byte getActivePipes()
    {
        return (byte)(this.itemMULTIBOOLPipes1.getMULTIBOOL() << 4 | this.itemMULTIBOOLPipes2.getMULTIBOOL());
    }

    public int getBackground()
    {
        return this.itemINTBackground.getINT();
    }

    public float getExplosionSize()
    {
        return (float)(this.itemINTExplosion.getINT() * 2);
    }

    public ArrayList<DropDownMenuItem> getList()
    {
        return this.items;
    }

    public SimulationInfo()
    {
        this.itemBOOLChest = new DropDownMenuItem("Chest", 0, DropDownMenuItem.VALUETYPE.BOOL, ModuleChest.class, ModuleInternalStorage.class);
        this.itemBOOLInvis = new DropDownMenuItem("Invisible", 1, DropDownMenuItem.VALUETYPE.BOOL, ModuleInvisible.class);
        this.itemBOOLBrake = new DropDownMenuItem("Brake", 2, DropDownMenuItem.VALUETYPE.BOOL, ModuleBrake.class);
        this.itemBOOLDrill = new DropDownMenuItem("Drill", 3, DropDownMenuItem.VALUETYPE.BOOL, ModuleDrill.class);
        this.itemBOOLLight = new DropDownMenuItem("Light", 4, DropDownMenuItem.VALUETYPE.BOOL, ModuleSolarBase.class);
        this.itemBOOLBridge = new DropDownMenuItem("Bridge", 5, DropDownMenuItem.VALUETYPE.BOOL, ModuleBridge.class);
        this.itemBOOLFarm = new DropDownMenuItem("Farm", 6, DropDownMenuItem.VALUETYPE.BOOL, ModuleFarmer.class);
        this.itemBOOLCut = new DropDownMenuItem("Cutting", 7, DropDownMenuItem.VALUETYPE.BOOL, ModuleWoodcutter.class);
        this.itemINTLiquid = new DropDownMenuItem("Liquid", 8, DropDownMenuItem.VALUETYPE.INT, ModuleLiquidSensors.class);
        this.itemINTLiquid.setINTLimit(1, 3);
        this.itemINTWater = new DropDownMenuItem("Water", 9, DropDownMenuItem.VALUETYPE.INT, ModuleHydrater.class);
        this.itemINTWater.setINTLimit(0, 4);
        this.itemINTFuse = new DropDownMenuItem("Fuse", 10, DropDownMenuItem.VALUETYPE.INT, ModuleDynamite.class);
        this.itemINTFuse.setINTLimit(1, 75);
        this.itemINTFuse.setINT(35);
        this.itemINTRail = new DropDownMenuItem("Rails", 11, DropDownMenuItem.VALUETYPE.INT, ModuleRailer.class);
        this.itemINTRail.setINTLimit(0, 6);
        this.itemINTExplosion = new DropDownMenuItem("Explosives", 12, DropDownMenuItem.VALUETYPE.INT, ModuleDynamite.class);
        this.itemINTExplosion.setINTLimit(4, 54);
        this.itemBOOLExplode = new DropDownMenuItem("Explode", 13, DropDownMenuItem.VALUETYPE.BOOL, ModuleDynamite.class);
        this.itemBOOLShield = new DropDownMenuItem("Shield", 14, DropDownMenuItem.VALUETYPE.BOOL, ModuleShield.class);
        this.itemBOOLShield.setBOOL(true);
        this.itemMULTIBOOLTorch = new DropDownMenuItem("Torches", 15, DropDownMenuItem.VALUETYPE.MULTIBOOL, ModuleTorch.class);
        this.itemMULTIBOOLTorch.setMULTIBOOLCount(3);
        this.itemMULTIBOOLPipes1 = new DropDownMenuItem("Pipes", 16, DropDownMenuItem.VALUETYPE.MULTIBOOL, ModuleShooter.class, ModuleShooterAdv.class);
        this.itemMULTIBOOLPipes1.setMULTIBOOLCount(4);
        this.itemMULTIBOOLPipes2 = new DropDownMenuItem("Pipes", 16, DropDownMenuItem.VALUETYPE.MULTIBOOL, ModuleShooter.class, ModuleShooterAdv.class);
        this.itemMULTIBOOLPipes2.setMULTIBOOLCount(4);
        this.itemBOOLPipe = new DropDownMenuItem("Pipe", 17, DropDownMenuItem.VALUETYPE.BOOL, ModuleShooterAdv.class);
        this.itemINTBackground = new DropDownMenuItem("Background", 18, DropDownMenuItem.VALUETYPE.INT, (Class)null);
        this.itemINTBackground.setINTLimit(StevesCarts.hasGreenScreen ? 0 : 1, 3);
        this.itemINTBackground.setINT(1);

        if (StevesCarts.hasGreenScreen)
        {
            this.items.add(this.itemINTBackground);
        }

        this.items.add(this.itemBOOLChest);
        this.items.add(this.itemBOOLInvis);
        this.items.add(this.itemBOOLBrake);
        this.items.add(this.itemBOOLDrill);
        this.items.add(this.itemBOOLLight);
        this.items.add(this.itemBOOLBridge);
        this.items.add(this.itemBOOLFarm);
        this.items.add(this.itemBOOLCut);
        this.items.add(this.itemINTLiquid);
        this.items.add(this.itemINTFuse);
        this.items.add(this.itemINTRail);
        this.items.add(this.itemINTExplosion);
        this.items.add(this.itemBOOLExplode);
        this.items.add(this.itemBOOLShield);
        this.items.add(this.itemMULTIBOOLTorch);
        this.items.add(this.itemMULTIBOOLPipes1);
        this.items.add(this.itemMULTIBOOLPipes2);
        this.items.add(this.itemBOOLPipe);
    }
}
