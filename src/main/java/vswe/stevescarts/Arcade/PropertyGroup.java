package vswe.stevescarts.Arcade;

import java.util.ArrayList;

public class PropertyGroup
{
    private ArrayList<Property> properties = new ArrayList();

    public ArrayList<Property> getProperties()
    {
        return this.properties;
    }

    public void add(Property property)
    {
        this.properties.add(property);
    }
}
