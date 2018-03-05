package Models;

import java.util.ArrayList;

public class ClassRoom {
    private static int DEFAULT_ID = -1;

    private int id;
    private String buildingName;
    private String roomNumber;
    private int capacity;
    private ArrayList<Section> sections;

    public ClassRoom(String buildingName, String roomNumber, int capacity) {
        this(DEFAULT_ID, buildingName, roomNumber, capacity);
    }

    public ClassRoom(int id, String buildingName, String roomNumber, int capacity) {
        this.id = id;
        this.buildingName = buildingName;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public boolean canFitCourse(Section section) {
        return this.getCapacity() >= section.getClassSize();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!ClassRoom.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final ClassRoom classRoom = (ClassRoom) obj;

        return classRoom.getId() == this.getId();
    }
}
