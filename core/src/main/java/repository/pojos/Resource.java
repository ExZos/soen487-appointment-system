package repository.pojos;

public class Resource {
    private int resourceId;
    private String name;

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
