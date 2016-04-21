package vswe.stevescarts.Helpers;

public abstract class LabelInformation
{
    private Localization.MODULES.ADDONS name;

    public LabelInformation(Localization.MODULES.ADDONS name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name.translate(new String[0]);
    }

    public abstract String getLabel();
}
