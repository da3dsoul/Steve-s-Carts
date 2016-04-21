package vswe.stevescarts.Slots;

import vswe.stevescarts.TileEntities.TileEntityCargo;

public class SlotCargo extends SlotBase implements ISpecialSlotValidator
{
    private TileEntityCargo cargo;
    private int id;

    public SlotCargo(TileEntityCargo cargo, int id)
    {
        super(cargo, id, -3000, -3000);
        this.id = id;
        this.cargo = cargo;
    }

    public boolean isSlotValid()
    {
        if (this.cargo.layoutType == 0)
        {
            return true;
        }
        else
        {
            int type;

            if (this.cargo.layoutType == 1)
            {
                type = this.cargo.getCurrentTransferForSlots().getSetting();
            }
            else
            {
                type = this.cargo.getCurrentTransferForSlots().getSide();
            }

            int slotType = this.id / 15;

            if (this.cargo.layoutType == 2)
            {
                slotType = this.cargo.color[slotType] - 1;
            }

            return slotType == type;
        }
    }

    public void updatePosition()
    {
        byte offset;

        if (this.cargo.layoutType == 0)
        {
            offset = 0;
        }
        else
        {
            offset = 5;
        }

        int x;
        int y;

        if (this.id < 15)
        {
            x = this.id % 5;
            y = this.id / 5;
            this.xDisplayPosition = 8 + x * 18;
            this.yDisplayPosition = 16 + y * 18 - offset;
        }
        else if (this.id < 30)
        {
            x = (this.id - 15) % 5 + 11;
            y = (this.id - 15) / 5;
            this.xDisplayPosition = 8 + x * 18;
            this.yDisplayPosition = 16 + y * 18 - offset;
        }
        else if (this.id < 45)
        {
            x = (this.id - 30) % 5;
            y = (this.id - 30) / 5 + 3;
            this.xDisplayPosition = 8 + x * 18;
            this.yDisplayPosition = 16 + y * 18 + offset;
        }
        else
        {
            x = (this.id - 45) % 5 + 11;
            y = (this.id - 45) / 5 + 3;
            this.xDisplayPosition = 8 + x * 18;
            this.yDisplayPosition = 16 + y * 18 + offset;
        }
    }
}
