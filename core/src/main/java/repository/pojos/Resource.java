package repository.pojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="resource")
@XmlAccessorType(XmlAccessType.FIELD)
public class Resource {
    @XmlElement
    protected int resourceId;
    @XmlElement
    protected String name;

    public Resource() { }

    public Resource(int resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }

    public int getResourceId(){ return resourceId;}
    public void setResourceId(int resourceId) { this.resourceId = resourceId;}

    public String getName(){ return name;}
    public void setName(String name) { this.name = name;}

    public String toString() {
        String str;
        return str = "ResourceID: " + resourceId + " , NAME: " + name + "\n";
    }
}
