// AdditionalService.java
package model.entities;

public class AdditionalService {
    private int id;
    private String name;
    private String description;

    public AdditionalService(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getteri i setteri
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}