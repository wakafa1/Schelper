package cn.wakafa.listview.Others;

/**
 * A class to implement notes from network
 */

public class Note{
    private String name;
    private int imageId;
    private int index;
    public Note(int index, String name, int imageId) {
        this.index = index;
        this.name = name;
        this.imageId = imageId;
    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }

    public int getIndex() {
        return index;
    }
}
