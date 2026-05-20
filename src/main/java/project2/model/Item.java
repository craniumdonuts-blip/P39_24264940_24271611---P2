
package project2.model;

/**
 *
 * @author angie
 */
public class Item {
    private String name;
    private String description;
    
    // constructor with item AND desc
    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }
    
    // constructor with just item for loading items from save data
    // as only name is stored in inventory save format
    public Item(String name){
        this(name, "No description available"); 
    }

    // getters so variables can be read/accessed by other classes
    public String getName(){
        return name;
    }
    
    public String getDescription(){
        return description;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Item){
            // convert from Object to Item to call other.getName()
            Item other = (Item)obj; 
            // checks if the passed in item (obj/other) name 
            // is equal to current object name
            return other.getName().equals(getName()); 
        } else {
            return false;
        }
    }
    
    // override hashCode() method because equals() was overridden
    // two objects that are equal must have the same hashcode
    @Override
    public int hashCode(){
        return name.hashCode();
    }
    
}