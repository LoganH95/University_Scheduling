package WebScraper;

import Models.ClassRoom;
import Storage.DatabaseConnection;
import com.jaunt.*;
import com.jaunt.component.Form;

public class ClassroomScraper {
    private static final String CLASSROOM_URL = "https://www.fpm.iastate.edu/roomscheduling/classrooms.asp";
    private DatabaseConnection databaseConnection;
    private UserAgent userAgent;

    public static void main(String args[]) {
        ClassroomScraper webScraper = new ClassroomScraper();
        webScraper.scrape();
    }

    private ClassroomScraper() {
        userAgent = new UserAgent();
        databaseConnection = new DatabaseConnection();
    }

    private void scrape() {
        try {
            for (int i = 0; i < 39; i++) {
                userAgent.visit(CLASSROOM_URL);
                Form form = userAgent.doc.getForm(0);
                form.setSelect("building", i);
                Document document = form.submit();
                Elements rows = document.findEvery("<table class=cream>").findEvery("<tr>");
                for (Element row : rows) {
                    ClassRoom classRoom = constructClassRoom(row, i);
                    if (classRoom != null) {
                        databaseConnection.exportClassRoom(classRoom);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ClassRoom constructClassRoom(Element row, int i) {
        try {
            Elements links = row.findEvery("<a>");
            if (links.size() == 0) {
                return null;
            }
            Element link = links.getElement(0);
            String roomInfo = fixRoomInfo(link.getText(), i);
            String buildingName = parseBuildingName(roomInfo);
            String roomNumber = parseRoomNumber(roomInfo);
            int capacity = Integer.parseInt(row.getChildElements().get(1).getText());
            return new ClassRoom(buildingName, roomNumber, capacity);
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        }
        return null;
    }

    private String parseBuildingName(String roomInfo) {
        return roomInfo.substring(0, roomInfo.lastIndexOf(" ")).trim();
    }

    private String parseRoomNumber(String roomInfo) {
        return roomInfo.substring(roomInfo.lastIndexOf(" ") + 1).trim();
    }

    private String fixRoomInfo(String roomInfo, int i) {
        //Handle cases without space between building name and room number
        String updatedInfo = roomInfo;
        if (i == 1) {
            updatedInfo = updatedInfo.replaceAll("ATANSFF", "ATANSFF ");
        } else if (i == 23) {
            updatedInfo = updatedInfo.replaceAll("LAGOMARE", "LAGOMARE ");
            updatedInfo = updatedInfo.replaceAll("LAGOMARW", "LAGOMARW ");
        }
        return updatedInfo;
    }

}
