package project2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author angie
 */
public class Inventory {

    // so the code knows that items is of type List and can use implementation
    // of List interface (ArrayList)
    private List<Item> items = new ArrayList<Item>();

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean hasItem(String name) {
        for (Item item : items) { // for each item in items arraylist...
            // get name of current item and if it's equal to name given...
            if (item.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // loop through arraylist then remove first item found if the name equals with given name
    public void removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(name)) {
                items.remove(i);
                return; // immediately exit method and stop loop
            }
        }
    }

    public void removeRandomItem() {
        Random random = new Random();

        if (!items.isEmpty()) {
            int r = random.nextInt(items.size());
            items.remove(r);
        }
    }

    // convert arraylist inventory to plain-text string for txt file saving
    public String toSaveString() {
        String string = "";

        for (int i = 0; i < items.size(); i++) {
            string = string + items.get(i).getName();
            if (i != items.size() - 1) {
                string = string + ",";
            }
        }

        return string;
    }

    // convert plain-text string version of inventory back to arraylist for txt file loading
    public void fromSaveString(String data) {
        items.clear();

        if (data.equals("")) {
            return;
        }

        String[] parts = data.split(",");
        for (int i = 0; i < parts.length; i++) {
            Item item = new Item(parts[i]);
            items.add(item);
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

}
