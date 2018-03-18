package Models;

public class ClassRoom {
    private static int DEFAULT_ID = -1;

    private int id;
    private String buildingName;
    private String roomNumber;
    private int capacity;

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

    @Override
    public String toString() {
        return buildingName + " " + roomNumber;
    }
}
