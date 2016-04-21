package vswe.stevescarts.Modules.Realtimers;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;
import vswe.stevescarts.Carts.MinecartModular;
import vswe.stevescarts.Helpers.Localization;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Interfaces.GuiMinecart;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleNote extends ModuleBase
{
    private final int maximumTracksPerModuleBitCount = 4;
    private final int maximumNotesPerTrackBitCount = 12;
    private int veryLongTrackLimit = 1024;
    private int notesInView = 13;
    private int tracksInView = 5;
    private int[] instrumentColors = new int[] {4210752, 16711680, 65280, 255, 16776960, 65535};
    private String[] pitchNames = new String[] {"F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#"};
    private Localization.MODULES.ATTACHMENTS[] instrumentNames;
    private ArrayList<ModuleNote.Track> tracks;
    private int notemapX;
    private int notemapY;
    private int trackHeight;
    private ArrayList<ModuleNote.Button> buttons;
    private ArrayList<ModuleNote.Button> instrumentbuttons;
    private int currentInstrument;
    private ModuleNote.Button createTrack;
    private ModuleNote.Button removeTrack;
    private ModuleNote.Button speedButton;
    private boolean isScrollingX;
    private boolean isScrollingXTune;
    private int scrollX;
    private boolean isScrollingY;
    private int scrollY;
    private int pixelScrollX;
    private int pixelScrollXTune;
    private int generatedScrollX;
    private int pixelScrollY;
    private int generatedScrollY;
    private int[] scrollXrect;
    private int[] scrollYrect;
    private final int maximumNotesPerTrack;
    private final int maximumTracksPerModule;
    private int currentTick;
    private int playProgress;
    private boolean tooLongTrack;
    private boolean tooTallModule;
    private boolean veryLongTrack;
    private int speedSetting;
    private short lastModuleHeader;

    public ModuleNote(MinecartModular cart)
    {
        super(cart);
        this.instrumentNames = new Localization.MODULES.ATTACHMENTS[] {Localization.MODULES.ATTACHMENTS.PIANO, Localization.MODULES.ATTACHMENTS.BASS_DRUM, Localization.MODULES.ATTACHMENTS.SNARE_DRUM, Localization.MODULES.ATTACHMENTS.STICKS, Localization.MODULES.ATTACHMENTS.BASS_GUITAR};
        this.notemapX = 70;
        this.notemapY = 40;
        this.trackHeight = 20;
        this.currentInstrument = -1;
        this.scrollXrect = new int[] {this.notemapX + 120, this.notemapY - 20, 100, 16};
        this.scrollYrect = new int[] {this.notemapX + 220, this.notemapY, 16, 100};
        this.currentTick = 0;
        this.playProgress = 0;
        this.tooLongTrack = false;
        this.tooTallModule = false;
        this.veryLongTrack = false;
        this.speedSetting = 5;
        this.maximumNotesPerTrack = (int)Math.pow(2.0D, 12.0D) - 1;
        this.maximumTracksPerModule = (int)Math.pow(2.0D, 4.0D) - 1;
        this.tracks = new ArrayList();

        if (this.getCart().worldObj.isRemote)
        {
            this.buttons = new ArrayList();
            this.createTrack = new ModuleNote.Button(this.notemapX - 60, this.notemapY - 20);
            this.createTrack.text = Localization.MODULES.ATTACHMENTS.CREATE_TRACK.translate(new String[0]);
            this.createTrack.imageID = 0;
            this.removeTrack = new ModuleNote.Button(this.notemapX - 40, this.notemapY - 20);
            this.removeTrack.text = Localization.MODULES.ATTACHMENTS.REMOVE_TRACK.translate(new String[0]);
            this.removeTrack.imageID = 1;
            this.speedButton = new ModuleNote.Button(this.notemapX - 20, this.notemapY - 20);
            this.updateSpeedButton();
            this.instrumentbuttons = new ArrayList();

            for (int i = 0; i < 6; ++i)
            {
                ModuleNote.Button tempButton = new ModuleNote.Button(this.notemapX - 20 + (i + 1) * 20, this.notemapY - 20);
                this.instrumentbuttons.add(tempButton);

                if (i > 0)
                {
                    tempButton.text = Localization.MODULES.ATTACHMENTS.ACTIVATE_INSTRUMENT.translate(new String[] {this.instrumentNames[i - 1].translate(new String[0])});
                }
                else
                {
                    tempButton.text = Localization.MODULES.ATTACHMENTS.DEACTIVATE_INSTRUMENT.translate(new String[0]);
                }

                tempButton.color = this.instrumentColors[i];
            }
        }
    }

    private void updateSpeedButton()
    {
        if (this.getCart().worldObj.isRemote)
        {
            this.speedButton.imageID = 14 - this.speedSetting;
            this.speedButton.text = Localization.MODULES.ATTACHMENTS.NOTE_DELAY.translate(new String[] {String.valueOf(this.getTickDelay())});
        }
    }

    public void drawForeground(GuiMinecart gui)
    {
        this.drawString(gui, this.getModuleName(), 8, 6, 4210752);

        for (int i = this.getScrollY(); i < Math.min(this.tracks.size(), this.getScrollY() + this.tracksInView); ++i)
        {
            ModuleNote.Track track = (ModuleNote.Track)this.tracks.get(i);

            for (int j = this.getScrollX(); j < Math.min(track.notes.size(), this.getScrollX() + this.notesInView); ++j)
            {
                ModuleNote.Note note = (ModuleNote.Note)track.notes.get(j);
                note.drawText(gui, i - this.getScrollY(), j - this.getScrollX());
            }
        }
    }

    public boolean hasSlots()
    {
        return false;
    }

    public boolean hasGui()
    {
        return true;
    }

    public void activatedByRail(int x, int y, int z, boolean active)
    {
        if (active && !this.isPlaying())
        {
            this.setPlaying(true);
        }
    }

    private int getTickDelay()
    {
        switch (this.speedSetting)
        {
            case 0:
                return 13;

            case 1:
                return 11;

            case 2:
                return 7;

            case 3:
                return 5;

            case 4:
                return 3;

            case 5:
                return 2;

            case 6:
                return 1;

            default:
                return 0;
        }
    }

    public void update()
    {
        super.update();

        if (this.getCart().worldObj.isRemote)
        {
            this.tooLongTrack = false;
            this.veryLongTrack = false;
            int found;

            for (found = 0; found < this.tracks.size(); ++found)
            {
                ModuleNote.Track i$ = (ModuleNote.Track)this.tracks.get(found);

                if (i$.notes.size() > this.notesInView)
                {
                    this.tooLongTrack = true;

                    if (i$.notes.size() > this.veryLongTrackLimit)
                    {
                        this.veryLongTrack = true;
                    }
                }

                byte track = -1;

                if (i$.addButton.down)
                {
                    i$.addButton.down = false;
                    track = 0;
                }
                else if (i$.removeButton.down)
                {
                    i$.removeButton.down = false;
                    track = 1;
                }
                else if (i$.volumeButton.down)
                {
                    i$.volumeButton.down = false;
                    track = 2;
                }

                if (track != -1)
                {
                    byte note = (byte)(found | track << 4);
                    this.sendPacket(1, note);
                }
            }

            if (!this.tooLongTrack)
            {
                this.pixelScrollX = 0;
                this.isScrollingX = false;
            }

            if (!this.veryLongTrack)
            {
                this.pixelScrollXTune = 0;
                this.isScrollingXTune = false;
            }

            this.tooTallModule = this.tracks.size() > this.tracksInView;

            if (!this.tooTallModule)
            {
                this.pixelScrollY = 0;
                this.isScrollingY = false;
            }

            this.generateScrollX();
            this.generateScrollY();

            if (this.createTrack.down)
            {
                this.createTrack.down = false;
                this.sendPacket(0, (byte)0);
            }

            if (this.removeTrack.down)
            {
                this.removeTrack.down = false;
                this.sendPacket(0, (byte)1);
            }

            if (this.speedButton.down)
            {
                this.speedButton.down = false;
                this.sendPacket(0, (byte)2);
            }

            for (found = 0; found < this.instrumentbuttons.size(); ++found)
            {
                if (((ModuleNote.Button)this.instrumentbuttons.get(found)).down && found != this.currentInstrument)
                {
                    this.currentInstrument = found;
                    break;
                }
            }

            for (found = 0; found < this.instrumentbuttons.size(); ++found)
            {
                if (((ModuleNote.Button)this.instrumentbuttons.get(found)).down && found != this.currentInstrument)
                {
                    ((ModuleNote.Button)this.instrumentbuttons.get(found)).down = false;
                }
            }

            if (this.currentInstrument != -1 && !((ModuleNote.Button)this.instrumentbuttons.get(this.currentInstrument)).down)
            {
                this.currentInstrument = -1;
            }
        }

        if (this.isPlaying())
        {
            if (this.currentTick <= 0)
            {
                boolean var6 = false;
                Iterator var7 = this.tracks.iterator();

                while (var7.hasNext())
                {
                    ModuleNote.Track var8 = (ModuleNote.Track)var7.next();

                    if (var8.notes.size() > this.playProgress)
                    {
                        ModuleNote.Note var9 = (ModuleNote.Note)var8.notes.get(this.playProgress);
                        float volume;

                        switch (var8.volume)
                        {
                            case 0:
                                volume = 0.0F;
                                break;

                            case 1:
                                volume = 0.33F;
                                break;

                            case 2:
                                volume = 0.67F;
                                break;

                            default:
                                volume = 1.0F;
                        }

                        var9.play(volume);
                        var6 = true;
                    }
                }

                if (!var6)
                {
                    if (!this.getCart().worldObj.isRemote)
                    {
                        this.setPlaying(false);
                    }

                    this.playProgress = 0;
                }
                else
                {
                    ++this.playProgress;
                }

                this.currentTick = this.getTickDelay() - 1;
            }
            else
            {
                --this.currentTick;
            }
        }
    }

    public int guiWidth()
    {
        return 310;
    }

    public int guiHeight()
    {
        return 150;
    }

    public void drawBackground(GuiMinecart gui, int x, int y)
    {
        ResourceHelper.bindResource("/gui/note.png");

        for (int marker = this.getScrollY(); marker < Math.min(this.tracks.size(), this.getScrollY() + this.tracksInView); ++marker)
        {
            ModuleNote.Track button = (ModuleNote.Track)this.tracks.get(marker);

            for (int j = this.getScrollX(); j < Math.min(button.notes.size(), this.getScrollX() + this.notesInView); ++j)
            {
                ModuleNote.Note note = (ModuleNote.Note)button.notes.get(j);
                note.draw(gui, x, y, marker - this.getScrollY(), j - this.getScrollX());
            }
        }

        Iterator var8 = this.buttons.iterator();

        while (var8.hasNext())
        {
            ModuleNote.Button var10 = (ModuleNote.Button)var8.next();
            var10.draw(gui, x, y);
        }

        int[] var9;

        if (this.tooLongTrack)
        {
            this.drawImage(gui, this.scrollXrect, 48, 0);
            var9 = this.getMarkerX();
            this.drawImage(gui, var9, 148, 1);

            if (this.veryLongTrack)
            {
                var9 = this.getMarkerXTune();
                this.drawImage(gui, var9, 153, 1);
            }
        }
        else
        {
            this.drawImage(gui, this.scrollXrect, 48, 16);
        }

        if (this.tooTallModule)
        {
            this.drawImage(gui, this.scrollYrect, 0, 48);
            var9 = this.getMarkerY();
            this.drawImage(gui, var9, 1, 148);
        }
        else
        {
            this.drawImage(gui, this.scrollYrect, 16, 48);
        }
    }

    private int[] getMarkerX()
    {
        return this.generateMarkerX(this.pixelScrollX);
    }

    private int[] getMarkerXTune()
    {
        return this.generateMarkerX(this.pixelScrollXTune);
    }

    private int[] generateMarkerX(int x)
    {
        return new int[] {this.scrollXrect[0] + x, this.scrollXrect[1] + 1, 5, 14};
    }

    private void setMarkerX(int x)
    {
        this.pixelScrollX = this.generateNewMarkerX(x);
    }

    private void setMarkerXTune(int x)
    {
        this.pixelScrollXTune = this.generateNewMarkerX(x);
    }

    private int generateNewMarkerX(int x)
    {
        int temp = x - this.scrollXrect[0];

        if (temp < 0)
        {
            temp = 0;
        }
        else if (temp > this.scrollXrect[2] - 5)
        {
            temp = this.scrollXrect[2] - 5;
        }

        return temp;
    }

    private int getScrollX()
    {
        return this.generatedScrollX;
    }

    private void generateScrollX()
    {
        if (this.tooLongTrack)
        {
            int maxNotes = -1;

            for (int widthOfBlockInScrollArea = 0; widthOfBlockInScrollArea < this.tracks.size(); ++widthOfBlockInScrollArea)
            {
                maxNotes = Math.max(maxNotes, ((ModuleNote.Track)this.tracks.get(widthOfBlockInScrollArea)).notes.size());
            }

            maxNotes -= this.notesInView;
            float var3 = (float)(this.scrollXrect[2] - 5) / (float)maxNotes;
            this.generatedScrollX = Math.round((float)this.pixelScrollX / var3);

            if (this.veryLongTrack)
            {
                this.generatedScrollX = (int)((float)this.generatedScrollX + (float)this.pixelScrollXTune / (float)(this.scrollXrect[2] - 5) * 50.0F);
            }
        }
        else
        {
            this.generatedScrollX = 0;
        }
    }

    private int[] getMarkerY()
    {
        return new int[] {this.scrollYrect[0] + 1, this.scrollYrect[1] + this.pixelScrollY, 14, 5};
    }

    private void setMarkerY(int y)
    {
        this.pixelScrollY = y - this.scrollYrect[1];

        if (this.pixelScrollY < 0)
        {
            this.pixelScrollY = 0;
        }
        else if (this.pixelScrollY > this.scrollYrect[3] - 5)
        {
            this.pixelScrollY = this.scrollYrect[3] - 5;
        }
    }

    private int getScrollY()
    {
        return this.generatedScrollY;
    }

    private void generateScrollY()
    {
        if (this.tooTallModule)
        {
            int maxTracks = this.tracks.size() - this.tracksInView;
            float heightOfBlockInScrollArea = (float)((this.scrollYrect[3] - 5) / maxTracks);
            this.generatedScrollY = Math.round((float)this.pixelScrollY / heightOfBlockInScrollArea);
        }
        else
        {
            this.generatedScrollY = 0;
        }
    }

    public void drawMouseOver(GuiMinecart gui, int x, int y)
    {
        for (int i$ = this.getScrollY(); i$ < Math.min(this.tracks.size(), this.getScrollY() + this.tracksInView); ++i$)
        {
            ModuleNote.Track button = (ModuleNote.Track)this.tracks.get(i$);

            for (int j = this.getScrollX(); j < Math.min(button.notes.size(), this.getScrollX() + this.notesInView); ++j)
            {
                ModuleNote.Note note = (ModuleNote.Note)button.notes.get(j);

                if (note.instrumentId != 0)
                {
                    this.drawStringOnMouseOver(gui, note.toString(), x, y, note.getBounds(i$ - this.getScrollY(), j - this.getScrollX()));
                }
            }
        }

        Iterator var8 = this.buttons.iterator();

        while (var8.hasNext())
        {
            ModuleNote.Button var9 = (ModuleNote.Button)var8.next();

            if (var9.text != null && var9.text.length() > 0)
            {
                var9.overlay(gui, x, y);
            }
        }
    }

    public void mouseMovedOrUp(GuiMinecart gui, int x, int y, int button)
    {
        if (this.isScrollingX)
        {
            this.setMarkerX(x);

            if (button != -1)
            {
                this.isScrollingX = false;
            }
        }

        if (this.isScrollingXTune)
        {
            this.setMarkerXTune(x);

            if (button != -1)
            {
                this.isScrollingXTune = false;
            }
        }

        if (this.isScrollingY)
        {
            this.setMarkerY(y + this.getCart().getRealScrollY());

            if (button != -1)
            {
                this.isScrollingY = false;
            }
        }
    }

    public void mouseClicked(GuiMinecart gui, int x, int y, int buttonId)
    {
        if (buttonId == 0)
        {
            Iterator i = this.buttons.iterator();

            while (i.hasNext())
            {
                ModuleNote.Button track = (ModuleNote.Button)i.next();
                track.clicked(x, y);
            }

            if (!this.isScrollingX && this.inRect(x, y, this.scrollXrect))
            {
                this.isScrollingX = true;
            }
            else if (!this.isScrollingY && this.inRect(x, y, this.scrollYrect))
            {
                this.isScrollingY = true;
            }
        }
        else if (buttonId == 1 && !this.isScrollingXTune && this.inRect(x, y, this.scrollXrect))
        {
            this.isScrollingXTune = true;
        }

        if (buttonId == 0 || buttonId == 1)
        {
            for (int var11 = this.getScrollY(); var11 < Math.min(this.tracks.size(), this.getScrollY() + this.tracksInView); ++var11)
            {
                ModuleNote.Track var12 = (ModuleNote.Track)this.tracks.get(var11);

                for (int j = this.getScrollX(); j < Math.min(var12.notes.size(), this.getScrollX() + this.notesInView); ++j)
                {
                    ModuleNote.Note note = (ModuleNote.Note)var12.notes.get(j);

                    if (this.inRect(x, y, note.getBounds(var11 - this.getScrollY(), j - this.getScrollX())))
                    {
                        int instrumentInfo = this.currentInstrument;

                        if (instrumentInfo == -1)
                        {
                            if (buttonId == 0)
                            {
                                instrumentInfo = 6;
                            }
                            else
                            {
                                instrumentInfo = 7;
                            }
                        }

                        if (this.currentInstrument != -1 || note.instrumentId != 0)
                        {
                            byte info = (byte)var11;
                            info = (byte)(info | instrumentInfo << 4);
                            this.sendPacket(2, new byte[] {info, (byte)j});
                        }
                    }
                }
            }
        }
    }

    public int numberOfGuiData()
    {
        return 1 + (this.maximumNotesPerTrack + 1) * this.maximumTracksPerModule;
    }

    protected void checkGuiData(Object[] info)
    {
        short moduleHeader = (short)this.tracks.size();
        moduleHeader |= (short)(this.speedSetting << 4);
        this.updateGuiData(info, 0, moduleHeader);

        for (int i = 0; i < this.tracks.size(); ++i)
        {
            ModuleNote.Track track = (ModuleNote.Track)this.tracks.get(i);
            this.updateGuiData(info, 1 + (this.maximumNotesPerTrack + 1) * i, track.getInfo());

            for (int j = 0; j < track.notes.size(); ++j)
            {
                ModuleNote.Note note = (ModuleNote.Note)track.notes.get(j);
                this.updateGuiData(info, 1 + (this.maximumNotesPerTrack + 1) * i + 1 + j, note.getInfo());
            }
        }
    }

    public void receiveGuiData(int id, short data)
    {
        int trackId;

        if (id == 0)
        {
            trackId = data & this.maximumTracksPerModule;
            this.speedSetting = (data & ~this.maximumTracksPerModule) >> 4;
            this.updateSpeedButton();

            while (this.tracks.size() < trackId)
            {
                new ModuleNote.Track();
            }

            while (this.tracks.size() > trackId)
            {
                ((ModuleNote.Track)this.tracks.get(this.tracks.size() - 1)).unload();
                this.tracks.remove(this.tracks.size() - 1);
            }
        }
        else
        {
            --id;
            trackId = id / (this.maximumNotesPerTrack + 1);
            int noteId = id % (this.maximumNotesPerTrack + 1);
            ModuleNote.Track track = (ModuleNote.Track)this.tracks.get(trackId);

            if (noteId == 0)
            {
                track.setInfo(data);
            }
            else
            {
                --noteId;
                ModuleNote.Note note = (ModuleNote.Note)track.notes.get(noteId);
                note.setInfo(data);
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

    private boolean isPlaying()
    {
        return this.isPlaceholder() ? false : this.getDw(0) != 0 || this.playProgress > 0;
    }

    private void setPlaying(boolean val)
    {
        this.updateDw(0, val ? 1 : 0);
    }

    public int numberOfPackets()
    {
        return 3;
    }

    protected void receivePacket(int id, byte[] data, EntityPlayer player)
    {
        if (id == 0)
        {
            if (data[0] == 0)
            {
                if (this.tracks.size() < this.maximumTracksPerModule)
                {
                    new ModuleNote.Track();
                }
            }
            else if (data[0] == 1)
            {
                if (this.tracks.size() > 0)
                {
                    this.tracks.remove(this.tracks.size() - 1);
                }
            }
            else if (data[0] == 2)
            {
                ++this.speedSetting;

                if (this.speedSetting >= 7)
                {
                    this.speedSetting = 0;
                }
            }
        }
        else if (id == 1)
        {
            int info = data[0] & this.maximumTracksPerModule;
            int noteID = (data[0] & ~this.maximumTracksPerModule) >> 4;

            if (info < this.tracks.size())
            {
                ModuleNote.Track trackID = (ModuleNote.Track)this.tracks.get(info);

                if (noteID == 0)
                {
                    if (trackID.notes.size() < this.maximumNotesPerTrack)
                    {
                        new ModuleNote.Note(trackID);
                    }
                }
                else if (noteID == 1)
                {
                    if (trackID.notes.size() > 0)
                    {
                        trackID.notes.remove(trackID.notes.size() - 1);
                    }
                }
                else if (noteID == 2)
                {
                    trackID.volume = (trackID.volume + 1) % 4;
                }
            }
        }
        else if (id == 2)
        {
            byte var10 = data[0];
            byte var11 = data[1];
            byte var12 = (byte)(var10 & this.maximumTracksPerModule);
            byte instrumentInfo = (byte)((byte)(var10 & ~((byte)this.maximumTracksPerModule)) >> 4);

            if (var12 < this.tracks.size())
            {
                ModuleNote.Track track = (ModuleNote.Track)this.tracks.get(var12);

                if (var11 < track.notes.size())
                {
                    ModuleNote.Note note = (ModuleNote.Note)track.notes.get(var11);

                    if (instrumentInfo < 6)
                    {
                        note.instrumentId = instrumentInfo;
                    }
                    else if (instrumentInfo == 6)
                    {
                        ++note.pitch;

                        if (note.pitch > 24)
                        {
                            note.pitch = 0;
                        }
                    }
                    else
                    {
                        --note.pitch;

                        if (note.pitch < 0)
                        {
                            note.pitch = 24;
                        }
                    }
                }
            }
        }
    }

    protected void Save(NBTTagCompound tagCompound, int id)
    {
        short headerInfo = (short)this.tracks.size();
        headerInfo |= (short)(this.speedSetting << 4);
        tagCompound.setShort(this.generateNBTName("Header", id), headerInfo);

        for (int i = 0; i < this.tracks.size(); ++i)
        {
            ModuleNote.Track track = (ModuleNote.Track)this.tracks.get(i);
            tagCompound.setShort(this.generateNBTName("Track" + i, id), track.getInfo());

            for (int j = 0; j < track.notes.size(); ++j)
            {
                ModuleNote.Note note = (ModuleNote.Note)track.notes.get(j);
                tagCompound.setShort(this.generateNBTName("Note" + i + ":" + j, id), note.getInfo());
            }
        }
    }

    protected void Load(NBTTagCompound tagCompound, int id)
    {
        short headerInfo = tagCompound.getShort(this.generateNBTName("Header", id));
        this.receiveGuiData(0, headerInfo);

        for (int i = 0; i < this.tracks.size(); ++i)
        {
            short trackInfo = tagCompound.getShort(this.generateNBTName("Track" + i, id));
            this.receiveGuiData(1 + (this.maximumNotesPerTrack + 1) * i, trackInfo);
            ModuleNote.Track track = (ModuleNote.Track)this.tracks.get(i);

            for (int j = 0; j < track.notes.size(); ++j)
            {
                short noteInfo = tagCompound.getShort(this.generateNBTName("Note" + i + ":" + j, id));
                this.receiveGuiData(1 + (this.maximumNotesPerTrack + 1) * i + 1 + j, noteInfo);
            }
        }
    }

    private class Button
    {
        public int[] rect;
        public boolean down = false;
        public String text;
        public int color;
        public int imageID;

        public Button(int x, int y)
        {
            this.rect = new int[] {x, y, 16, 16};
            this.color = 0;
            this.imageID = -1;
            ModuleNote.this.buttons.add(this);
        }

        public int[] getRect()
        {
            return this.rect;
        }

        public void overlay(GuiMinecart gui, int x, int y)
        {
            ModuleNote.this.drawStringOnMouseOver(gui, this.text, x, y, this.getRect());
        }

        public void clicked(int x, int y)
        {
            if (ModuleNote.this.inRect(x, y, this.getRect()))
            {
                this.down = !this.down;
            }
        }

        public void draw(GuiMinecart gui, int x, int y)
        {
            if (!ModuleNote.this.inRect(x, y, this.getRect()))
            {
                GL11.glColor4f((float)(this.color >> 16) / 255.0F, (float)(this.color >> 8 & 255) / 255.0F, (float)(this.color & 255) / 255.0F, 1.0F);
            }

            ModuleNote.this.drawImage(gui, this.getRect(), 32, 0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int srcX = 0;
            byte srcY = 16;

            if (this.down)
            {
                srcX += 16;
            }

            ModuleNote.this.drawImage(gui, this.getRect(), srcX, srcY);

            if (this.imageID != -1)
            {
                ModuleNote.this.drawImage(gui, this.getRect(), this.imageID * 16, 32);
            }
        }
    }

    private class Note
    {
        public int instrumentId;
        public int pitch;

        public Note(ModuleNote.Track track)
        {
            track.notes.add(this);
        }

        public void drawText(GuiMinecart gui, int trackID, int noteID)
        {
            if (this.instrumentId != 0)
            {
                int[] rect = this.getBounds(trackID, noteID);
                String str = String.valueOf(this.pitch);

                if (str.length() < 2)
                {
                    str = "0" + str;
                }

                ModuleNote.this.drawString(gui, str, rect[0] + 3, rect[1] + 6, ModuleNote.this.instrumentColors[this.instrumentId]);
            }
        }

        public void draw(GuiMinecart gui, int x, int y, int trackID, int noteID)
        {
            int srcX = 0;

            if (this.instrumentId == 0)
            {
                srcX += 16;
            }

            int[] rect = this.getBounds(trackID, noteID);

            if (this.instrumentId != 0 && ModuleNote.this.playProgress == noteID + ModuleNote.this.getScrollX() && ModuleNote.this.isPlaying())
            {
                GL11.glColor4f(0.3F, 0.3F, 0.3F, 1.0F);
            }

            ModuleNote.this.drawImage(gui, rect, srcX, 0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (ModuleNote.this.inRect(x, y, rect))
            {
                ModuleNote.this.drawImage(gui, rect, 32, 0);
            }
        }

        public int[] getBounds(int trackID, int noteID)
        {
            return new int[] {ModuleNote.this.notemapX + noteID * 16, ModuleNote.this.notemapY + trackID * ModuleNote.this.trackHeight, 16, 16};
        }

        public short getInfo()
        {
            byte info = 0;
            short info1 = (short)(info | this.instrumentId);
            info1 = (short)(info1 | this.pitch << 3);
            return info1;
        }

        public void setInfo(short val)
        {
            this.instrumentId = val & 7;
            this.pitch = (val & 248) >> 3;
        }

        public void play(float volume)
        {
            if (this.instrumentId != 0)
            {
                if (!ModuleNote.this.getCart().worldObj.isRemote)
                {
                    if (volume > 0.0F)
                    {
                        float oX = (float)Math.pow(2.0D, (double)(this.pitch - 12) / 12.0D);
                        String instrumentString = "harp";

                        if (this.instrumentId == 2)
                        {
                            instrumentString = "bd";
                        }
                        else if (this.instrumentId == 3)
                        {
                            instrumentString = "snare";
                        }
                        else if (this.instrumentId == 4)
                        {
                            instrumentString = "hat";
                        }
                        else if (this.instrumentId == 5)
                        {
                            instrumentString = "bassattack";
                        }

                        ModuleNote.this.getCart().worldObj.playSoundEffect((double)ModuleNote.this.getCart().x() + 0.5D, (double)ModuleNote.this.getCart().y() + 0.5D, (double)ModuleNote.this.getCart().z() + 0.5D, "note." + instrumentString, volume, oX);
                    }
                }
                else
                {
                    double oX1 = 0.0D;
                    double oZ = 0.0D;

                    if (ModuleNote.this.getCart().motionX != 0.0D)
                    {
                        oX1 = (double)(ModuleNote.this.getCart().motionX > 0.0D ? -1 : 1);
                    }

                    if (ModuleNote.this.getCart().motionZ != 0.0D)
                    {
                        oZ = (double)(ModuleNote.this.getCart().motionZ > 0.0D ? -1 : 1);
                    }

                    ModuleNote.this.getCart().worldObj.spawnParticle("note", (double)ModuleNote.this.getCart().x() + oZ * 1.0D + 0.5D, (double)ModuleNote.this.getCart().y() + 1.2D, (double)ModuleNote.this.getCart().z() + oX1 * 1.0D + 0.5D, (double)this.pitch / 24.0D, 0.0D, 0.0D);
                    ModuleNote.this.getCart().worldObj.spawnParticle("note", (double)ModuleNote.this.getCart().x() + oZ * -1.0D + 0.5D, (double)ModuleNote.this.getCart().y() + 1.2D, (double)ModuleNote.this.getCart().z() + oX1 * -1.0D + 0.5D, (double)this.pitch / 24.0D, 0.0D, 0.0D);
                }
            }
        }

        public String toString()
        {
            return this.instrumentId == 0 ? "Unknown instrument" : ModuleNote.this.instrumentNames[this.instrumentId - 1].translate(new String[0]) + " " + ModuleNote.this.pitchNames[this.pitch];
        }
    }

    private class Track
    {
        public ArrayList<ModuleNote.Note> notes = new ArrayList();
        public ModuleNote.Button addButton;
        public ModuleNote.Button removeButton;
        public ModuleNote.Button volumeButton;
        public int volume = 3;
        public int lastNoteCount;

        public Track()
        {
            if (ModuleNote.this.getCart().worldObj.isRemote)
            {
                int ID = ModuleNote.this.tracks.size() + 1;
                this.addButton = ModuleNote.this.new TrackButton(ModuleNote.this.notemapX - 60, ID - 1);
                this.addButton.text = Localization.MODULES.ATTACHMENTS.ADD_NOTE.translate(new String[] {String.valueOf(ID)});
                this.addButton.imageID = 2;
                this.removeButton = ModuleNote.this.new TrackButton(ModuleNote.this.notemapX - 40, ID - 1);
                this.removeButton.text = Localization.MODULES.ATTACHMENTS.REMOVE_NOTE.translate(new String[] {String.valueOf(ID)});
                this.removeButton.imageID = 3;
                this.volumeButton = ModuleNote.this.new TrackButton(ModuleNote.this.notemapX - 20, ID - 1);
                this.volumeButton.text = this.getVolumeText();
                this.volumeButton.imageID = 4;
            }

            ModuleNote.this.tracks.add(this);
        }

        private String getVolumeText()
        {
            return Localization.MODULES.ATTACHMENTS.VOLUME.translate(new String[] {String.valueOf(this.volume)});
        }

        public void unload()
        {
            ModuleNote.this.buttons.remove(this.addButton);
            ModuleNote.this.buttons.remove(this.removeButton);
            ModuleNote.this.buttons.remove(this.volumeButton);
        }

        public short getInfo()
        {
            byte info = 0;
            short info1 = (short)(info | this.notes.size());
            info1 = (short)(info1 | this.volume << 12);
            return info1;
        }

        public void setInfo(short val)
        {
            int numberofNotes = val & ModuleNote.this.maximumNotesPerTrack;

            while (this.notes.size() < numberofNotes)
            {
                ModuleNote.this.new Note(this);
            }

            while (this.notes.size() > numberofNotes)
            {
                this.notes.remove(this.notes.size() - 1);
            }

            this.volume = (val & ~ModuleNote.this.maximumNotesPerTrack) >> 12;

            if (ModuleNote.this.getCart().worldObj.isRemote)
            {
                this.volumeButton.imageID = 4 + this.volume;
                this.volumeButton.text = this.getVolumeText();
            }
        }
    }

    private class TrackButton extends ModuleNote.Button
    {
        private int trackID;
        private int x;

        public TrackButton(int x, int trackID)
        {
            super(0, 0);
            this.trackID = trackID;
            this.x = x;
        }

        public int[] getRect()
        {
            return new int[] {this.x, ModuleNote.this.notemapY + (this.trackID - ModuleNote.this.getScrollY()) * ModuleNote.this.trackHeight, 16, 16};
        }

        private boolean isValid()
        {
            return ModuleNote.this.getScrollY() <= this.trackID && this.trackID < ModuleNote.this.getScrollY() + ModuleNote.this.tracksInView;
        }

        public void draw(GuiMinecart gui, int x, int y)
        {
            if (this.isValid())
            {
                super.draw(gui, x, y);
            }
        }

        public void overlay(GuiMinecart gui, int x, int y)
        {
            if (this.isValid())
            {
                super.overlay(gui, x, y);
            }
        }

        public void clicked(int x, int y)
        {
            if (this.isValid())
            {
                super.clicked(x, y);
            }
        }
    }
}
