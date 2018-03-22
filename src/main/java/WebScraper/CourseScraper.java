package WebScraper;

import Models.Course;
import Models.Department;
import Models.Section;
import Storage.DatabaseConnection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CourseScraper {
    private static String API_URL = "http://classes.iastate.edu/app/rest/courses/preferences";
    private JSONArray departmentsJSON = new JSONArray("[{\"id\":\"1\",\"title\":\"ACCOUNTING\",\"abbreviation\":\"ACCT\"},{\"id\":\"2\",\"title\":\"ADVERTISING\",\"abbreviation\":\"ADVRT\"},{\"id\":\"3\",\"title\":\"AEROSPACE ENGINEERING\",\"abbreviation\":\"AER E\"},{\"id\":\"4\",\"title\":\"AFRICAN AND AFRICAN AMERICAN STUDIES\",\"abbreviation\":\"AF AM\"},{\"id\":\"5\",\"title\":\"AGRICULTURAL AND BIOSYSTEMS ENGINEERING\",\"abbreviation\":\"A B E\"},{\"id\":\"6\",\"title\":\"AGRICULTURAL EDUCATION & STUDIES\",\"abbreviation\":\"AGEDS\"},{\"id\":\"7\",\"title\":\"AGRONOMY\",\"abbreviation\":\"AGRON\"},{\"id\":\"8\",\"title\":\"AIR FORCE AEROSPACE STUDIES\",\"abbreviation\":\"AFAS\"},{\"id\":\"9\",\"title\":\"AMERICAN INDIAN STUDIES\",\"abbreviation\":\"AM IN\"},{\"id\":\"10\",\"title\":\"AMERICAN SIGN LANGUAGE\",\"abbreviation\":\"ASL\"},{\"id\":\"11\",\"title\":\"ANIMAL ECOLOGY\",\"abbreviation\":\"A ECL\"},{\"id\":\"12\",\"title\":\"ANIMAL SCIENCE\",\"abbreviation\":\"AN S\"},{\"id\":\"13\",\"title\":\"ANTHROPOLOGY\",\"abbreviation\":\"ANTHR\"},{\"id\":\"14\",\"title\":\"APPAREL, EVENTS, AND HOSPITALITY MANAGEM\",\"abbreviation\":\"AESHM\"},{\"id\":\"15\",\"title\":\"APPAREL, MERCHANDISING, AND DESIGN\",\"abbreviation\":\"A M D\"},{\"id\":\"16\",\"title\":\"ARABIC\",\"abbreviation\":\"ARABC\"},{\"id\":\"17\",\"title\":\"ARCHITECTURE\",\"abbreviation\":\"ARCH\"},{\"id\":\"18\",\"title\":\"ART EDUCATION\",\"abbreviation\":\"ARTED\"},{\"id\":\"19\",\"title\":\"ART HISTORY\",\"abbreviation\":\"ART H\"},{\"id\":\"20\",\"title\":\"ASTRONOMY & ASTROPHYSICS\",\"abbreviation\":\"ASTRO\"},{\"id\":\"21\",\"title\":\"ATHLETIC TRAINING\",\"abbreviation\":\"A TR\"},{\"id\":\"22\",\"title\":\"ATHLETICS\",\"abbreviation\":\"ATH\"},{\"id\":\"23\",\"title\":\"BIOCHEM, BIOPHYSICS & MOLECULAR BIO\",\"abbreviation\":\"BBMB\"},{\"id\":\"24\",\"title\":\"BIOINFORMATICS & COMPUTATIONAL BIOL-UG\",\"abbreviation\":\"BCBIO\"},{\"id\":\"25\",\"title\":\"BIOINFORMATICS AND COMPUTATIONAL BIOL\",\"abbreviation\":\"BCB\"},{\"id\":\"26\",\"title\":\"BIOLOGICAL/PRE-MEDICAL ILLUSTRATION\",\"abbreviation\":\"BPM I\"},{\"id\":\"27\",\"title\":\"BIOLOGY\",\"abbreviation\":\"BIOL\"},{\"id\":\"28\",\"title\":\"BIOMEDICAL ENGINEERING\",\"abbreviation\":\"B M E\"},{\"id\":\"29\",\"title\":\"BIOMEDICAL SCIENCES\",\"abbreviation\":\"B M S\"},{\"id\":\"30\",\"title\":\"BIORENEWABLE CHEMICALS\",\"abbreviation\":\"BR C\"},{\"id\":\"31\",\"title\":\"BIORENEWABLE RESOURCES & TECHNOLOGY\",\"abbreviation\":\"BRT\"},{\"id\":\"32\",\"title\":\"BUSINESS ADMINISTRATION\",\"abbreviation\":\"BUSAD\"},{\"id\":\"33\",\"title\":\"CHEMICAL ENGINEERING\",\"abbreviation\":\"CH E\"},{\"id\":\"34\",\"title\":\"CHEMISTRY\",\"abbreviation\":\"CHEM\"},{\"id\":\"35\",\"title\":\"CHINESE\",\"abbreviation\":\"CHIN\"},{\"id\":\"36\",\"title\":\"CIVIL ENGINEERING\",\"abbreviation\":\"C E\"},{\"id\":\"37\",\"title\":\"CLASSICAL STUDIES\",\"abbreviation\":\"CL ST\"},{\"id\":\"38\",\"title\":\"COMMUNICATION DISORDERS\",\"abbreviation\":\"CMDIS\"},{\"id\":\"39\",\"title\":\"COMMUNICATION STUDIES\",\"abbreviation\":\"COMST\"},{\"id\":\"40\",\"title\":\"COMMUNITY & REGIONAL PLANNING\",\"abbreviation\":\"C R P\"},{\"id\":\"41\",\"title\":\"COMMUNITY DEVELOPMENT\",\"abbreviation\":\"C DEV\"},{\"id\":\"42\",\"title\":\"COMPUTER ENGINEERING\",\"abbreviation\":\"CPR E\"},{\"id\":\"43\",\"title\":\"COMPUTER SCIENCE\",\"abbreviation\":\"COM S\"},{\"id\":\"44\",\"title\":\"CONSTRUCTION ENGINEERING\",\"abbreviation\":\"CON E\"},{\"id\":\"45\",\"title\":\"CRIMINAL JUSTICE STUDIES\",\"abbreviation\":\"CJ ST\"},{\"id\":\"46\",\"title\":\"CURRICULUM & INSTRUCTION\",\"abbreviation\":\"C I\"},{\"id\":\"47\",\"title\":\"DANCE\",\"abbreviation\":\"DANCE\"},{\"id\":\"48\",\"title\":\"DATA SCIENCE\",\"abbreviation\":\"DS\"},{\"id\":\"49\",\"title\":\"DESIGN\",\"abbreviation\":\"DES\"},{\"id\":\"50\",\"title\":\"DESIGN STUDIES\",\"abbreviation\":\"DSN S\"},{\"id\":\"51\",\"title\":\"DIETETICS\",\"abbreviation\":\"DIET\"},{\"id\":\"52\",\"title\":\"EARLY CHILDCARE EDUCATION AND PROGRAMMIN\",\"abbreviation\":\"E C P\"},{\"id\":\"53\",\"title\":\"ECOLOGY & EVOLUTIONARY BIOLOGY\",\"abbreviation\":\"EEB\"},{\"id\":\"54\",\"title\":\"ECOLOGY, EVOLUTION & ORGANISMAL BIOL\",\"abbreviation\":\"EEOB\"},{\"id\":\"55\",\"title\":\"ECONOMICS\",\"abbreviation\":\"ECON\"},{\"id\":\"56\",\"title\":\"EDUCATIONAL ADMINISTRATION\",\"abbreviation\":\"EDADM\"},{\"id\":\"57\",\"title\":\"EDUCATIONAL LEADERSHIP & POLICY ST\",\"abbreviation\":\"EL PS\"},{\"id\":\"58\",\"title\":\"ELECTRICAL ENGINEERING\",\"abbreviation\":\"E E\"},{\"id\":\"59\",\"title\":\"ENGINEERING\",\"abbreviation\":\"ENGR\"},{\"id\":\"60\",\"title\":\"ENGINEERING MECHANICS\",\"abbreviation\":\"E M\"},{\"id\":\"61\",\"title\":\"ENGLISH\",\"abbreviation\":\"ENGL\"},{\"id\":\"62\",\"title\":\"ENTOMOLOGY\",\"abbreviation\":\"ENT\"},{\"id\":\"63\",\"title\":\"ENTREPRENEURSHIP\",\"abbreviation\":\"ENTSP\"},{\"id\":\"64\",\"title\":\"ENVIRONMENTAL SCIENCE\",\"abbreviation\":\"ENSCI\"},{\"id\":\"65\",\"title\":\"ENVIRONMENTAL STUDIES\",\"abbreviation\":\"ENV S\"},{\"id\":\"66\",\"title\":\"EVENT MANAGEMENT\",\"abbreviation\":\"EVENT\"},{\"id\":\"67\",\"title\":\"FAMILY & CONSUMER SCIENCES ED & STUDIES\",\"abbreviation\":\"FCEDS\"},{\"id\":\"68\",\"title\":\"FAMILY FINANCIAL PLANNING\",\"abbreviation\":\"FFP\"},{\"id\":\"69\",\"title\":\"FINANCE\",\"abbreviation\":\"FIN\"},{\"id\":\"70\",\"title\":\"FOOD SCIENCE & HUMAN NUTRITION\",\"abbreviation\":\"FS HN\"},{\"id\":\"71\",\"title\":\"FORESTRY\",\"abbreviation\":\"FOR\"},{\"id\":\"72\",\"title\":\"FRENCH\",\"abbreviation\":\"FRNCH\"},{\"id\":\"73\",\"title\":\"GENETICS\",\"abbreviation\":\"GEN\"},{\"id\":\"74\",\"title\":\"GENETICS - INTERDISCIPLINARY\",\"abbreviation\":\"GENET\"},{\"id\":\"75\",\"title\":\"GENETICS, DEVELOPMENT & CELL BIOL\",\"abbreviation\":\"GDCB\"},{\"id\":\"76\",\"title\":\"GEOLOGY\",\"abbreviation\":\"GEOL\"},{\"id\":\"77\",\"title\":\"GERMAN\",\"abbreviation\":\"GER\"},{\"id\":\"78\",\"title\":\"GERONTOLOGY\",\"abbreviation\":\"GERON\"},{\"id\":\"79\",\"title\":\"GLOBAL RESOURCE SYSTEMS\",\"abbreviation\":\"GLOBE\"},{\"id\":\"80\",\"title\":\"GRADUATE STUDIES\",\"abbreviation\":\"GR ST\"},{\"id\":\"81\",\"title\":\"GRAPHIC DESIGN\",\"abbreviation\":\"ARTGR\"},{\"id\":\"82\",\"title\":\"GREEK\",\"abbreviation\":\"GREEK\"},{\"id\":\"83\",\"title\":\"HEALTH STUDIES\",\"abbreviation\":\"H S\"},{\"id\":\"84\",\"title\":\"HIGHER EDUCATION\",\"abbreviation\":\"HG ED\"},{\"id\":\"85\",\"title\":\"HISTORICAL, PHILOSOPHICAL & COMP ED\",\"abbreviation\":\"H P C\"},{\"id\":\"86\",\"title\":\"HISTORY\",\"abbreviation\":\"HIST\"},{\"id\":\"87\",\"title\":\"HONORS\",\"abbreviation\":\"HON\"},{\"id\":\"88\",\"title\":\"HORTICULTURE\",\"abbreviation\":\"HORT\"},{\"id\":\"89\",\"title\":\"HOSPITALITY MANAGEMENT\",\"abbreviation\":\"HSP M\"},{\"id\":\"90\",\"title\":\"HUMAN COMPUTER INTERACTION\",\"abbreviation\":\"HCI\"},{\"id\":\"91\",\"title\":\"HUMAN DEVELOPMENT & FAMILY STUDIES\",\"abbreviation\":\"HD FS\"},{\"id\":\"92\",\"title\":\"HUMAN SCIENCES\",\"abbreviation\":\"H SCI\"},{\"id\":\"93\",\"title\":\"IMMUNOBIOLOGY\",\"abbreviation\":\"IMBIO\"},{\"id\":\"94\",\"title\":\"INDUSTRIAL DESIGN\",\"abbreviation\":\"IND D\"},{\"id\":\"95\",\"title\":\"INDUSTRIAL ENGINEERING\",\"abbreviation\":\"I E\"},{\"id\":\"96\",\"title\":\"INFORMATION ASSURANCE\",\"abbreviation\":\"INFAS\"},{\"id\":\"97\",\"title\":\"INTEGRATED STUDIO ARTS\",\"abbreviation\":\"ARTIS\"},{\"id\":\"98\",\"title\":\"INTERDISCIPLINARY GRADUATE STUDIES\",\"abbreviation\":\"IGS\"},{\"id\":\"99\",\"title\":\"INTERIOR DESIGN\",\"abbreviation\":\"ARTID\"},{\"id\":\"100\",\"title\":\"INTERNATIONAL STUDIES\",\"abbreviation\":\"INTST\"},{\"id\":\"101\",\"title\":\"IOWA LAKESIDE LABORATORY\",\"abbreviation\":\"IA LL\"},{\"id\":\"102\",\"title\":\"ITALIAN\",\"abbreviation\":\"ITAL\"},{\"id\":\"103\",\"title\":\"JOURNALISM & MASS COMMUNICATION\",\"abbreviation\":\"JL MC\"},{\"id\":\"104\",\"title\":\"KINESIOLOGY\",\"abbreviation\":\"KIN\"},{\"id\":\"105\",\"title\":\"LANDSCAPE ARCHITECTURE\",\"abbreviation\":\"L A\"},{\"id\":\"106\",\"title\":\"LATIN\",\"abbreviation\":\"LATIN\"},{\"id\":\"107\",\"title\":\"LEADERSHIP STUDIES\",\"abbreviation\":\"LD ST\"},{\"id\":\"108\",\"title\":\"LEARNING AND LEADERSHIP SCIENCES\",\"abbreviation\":\"L L S\"},{\"id\":\"109\",\"title\":\"LEARNING TEAM\",\"abbreviation\":\"L TM\"},{\"id\":\"110\",\"title\":\"LIBERAL ARTS & SCIENCES\",\"abbreviation\":\"LAS\"},{\"id\":\"111\",\"title\":\"LIBRARY\",\"abbreviation\":\"LIB\"},{\"id\":\"112\",\"title\":\"LINGUISTICS\",\"abbreviation\":\"LING\"},{\"id\":\"113\",\"title\":\"MANAGEMENT\",\"abbreviation\":\"MGMT\"},{\"id\":\"114\",\"title\":\"MANAGEMENT INFORMATION SYSTEMS\",\"abbreviation\":\"MIS\"},{\"id\":\"115\",\"title\":\"MARKETING\",\"abbreviation\":\"MKT\"},{\"id\":\"116\",\"title\":\"MATERIALS ENGINEERING\",\"abbreviation\":\"MAT E\"},{\"id\":\"117\",\"title\":\"MATERIALS SCIENCE & ENGINEERING\",\"abbreviation\":\"M S E\"},{\"id\":\"118\",\"title\":\"MATHEMATICS\",\"abbreviation\":\"MATH\"},{\"id\":\"119\",\"title\":\"MECHANICAL ENGINEERING\",\"abbreviation\":\"M E\"},{\"id\":\"120\",\"title\":\"METEOROLOGY\",\"abbreviation\":\"MTEOR\"},{\"id\":\"121\",\"title\":\"MICROBIOLOGY\",\"abbreviation\":\"MICRO\"},{\"id\":\"122\",\"title\":\"MILITARY SCIENCE\",\"abbreviation\":\"M S\"},{\"id\":\"123\",\"title\":\"MOLECULAR CELLULAR DEV BIOLOGY\",\"abbreviation\":\"MCDB\"},{\"id\":\"124\",\"title\":\"MUSIC\",\"abbreviation\":\"MUSIC\"},{\"id\":\"125\",\"title\":\"NATURAL RESOURCE ECOLOGY AND MGMT\",\"abbreviation\":\"NREM\"},{\"id\":\"126\",\"title\":\"NAVAL SCIENCE\",\"abbreviation\":\"N S\"},{\"id\":\"127\",\"title\":\"NEUROSCIENCE\",\"abbreviation\":\"NEURO\"},{\"id\":\"128\",\"title\":\"NUCLEAR ENGINEERING\",\"abbreviation\":\"NUC E\"},{\"id\":\"129\",\"title\":\"NUTRITIONAL SCIENCES\",\"abbreviation\":\"NUTRS\"},{\"id\":\"130\",\"title\":\"ORGANIZATION FOR TROPICAL STUDIES\",\"abbreviation\":\"OTS\"},{\"id\":\"131\",\"title\":\"PERFORMING ARTS\",\"abbreviation\":\"PERF\"},{\"id\":\"132\",\"title\":\"PHILOSOPHY\",\"abbreviation\":\"PHIL\"},{\"id\":\"133\",\"title\":\"PHYSICS\",\"abbreviation\":\"PHYS\"},{\"id\":\"134\",\"title\":\"PLANT BIOLOGY\",\"abbreviation\":\"PLBIO\"},{\"id\":\"135\",\"title\":\"PLANT PATHOLOGY\",\"abbreviation\":\"PL P\"},{\"id\":\"136\",\"title\":\"POLITICAL SCIENCE\",\"abbreviation\":\"POL S\"},{\"id\":\"137\",\"title\":\"PORTUGUESE\",\"abbreviation\":\"PORT\"},{\"id\":\"138\",\"title\":\"PSYCHOLOGY\",\"abbreviation\":\"PSYCH\"},{\"id\":\"139\",\"title\":\"PUBLIC RELATIONS\",\"abbreviation\":\"P R\"},{\"id\":\"140\",\"title\":\"RELIGIOUS STUDIES\",\"abbreviation\":\"RELIG\"},{\"id\":\"141\",\"title\":\"RESEARCH & EVALUATION\",\"abbreviation\":\"RESEV\"},{\"id\":\"142\",\"title\":\"RUSSIAN\",\"abbreviation\":\"RUS\"},{\"id\":\"143\",\"title\":\"SEED TECHNOLOGY AND BUSINESS\",\"abbreviation\":\"STB\"},{\"id\":\"144\",\"title\":\"SOCIOLOGY\",\"abbreviation\":\"SOC\"},{\"id\":\"145\",\"title\":\"SOFTWARE ENGINEERING\",\"abbreviation\":\"S E\"},{\"id\":\"146\",\"title\":\"SPANISH\",\"abbreviation\":\"SPAN\"},{\"id\":\"147\",\"title\":\"SPECIAL EDUCATION\",\"abbreviation\":\"SP ED\"},{\"id\":\"148\",\"title\":\"SPEECH COMMUNICATION\",\"abbreviation\":\"SP CM\"},{\"id\":\"149\",\"title\":\"STATISTICS\",\"abbreviation\":\"STAT\"},{\"id\":\"150\",\"title\":\"SUPPLY CHAIN MANAGEMENT\",\"abbreviation\":\"SCM\"},{\"id\":\"151\",\"title\":\"SUSTAINABLE AGRICULTURE\",\"abbreviation\":\"SUSAG\"},{\"id\":\"152\",\"title\":\"SUSTAINABLE ENVIRONMENTS\",\"abbreviation\":\"SUS E\"},{\"id\":\"153\",\"title\":\"TECHNOLOGY & SOCIAL CHANGE\",\"abbreviation\":\"T SC\"},{\"id\":\"154\",\"title\":\"TECHNOLOGY SYSTEMS MANAGEMENT\",\"abbreviation\":\"TSM\"},{\"id\":\"155\",\"title\":\"THEATRE\",\"abbreviation\":\"THTRE\"},{\"id\":\"156\",\"title\":\"TOXICOLOGY\",\"abbreviation\":\"TOX\"},{\"id\":\"157\",\"title\":\"TRANSPORTATION\",\"abbreviation\":\"TRANS\"},{\"id\":\"158\",\"title\":\"U.S. LATINO/A STUDIES\",\"abbreviation\":\"US LS\"},{\"id\":\"159\",\"title\":\"UNIVERSITY STUDIES\",\"abbreviation\":\"U ST\"},{\"id\":\"160\",\"title\":\"URBAN DESIGN\",\"abbreviation\":\"URB D\"},{\"id\":\"161\",\"title\":\"VET DIAGNOSTIC & PRODUCTION AN MED\",\"abbreviation\":\"VDPAM\"},{\"id\":\"162\",\"title\":\"VETERINARY CLINICAL SCIENCES\",\"abbreviation\":\"V C S\"},{\"id\":\"163\",\"title\":\"VETERINARY MICROBIOLOGY & PREV MED\",\"abbreviation\":\"V MPM\"},{\"id\":\"164\",\"title\":\"VETERINARY PATHOLOGY\",\"abbreviation\":\"V PTH\"},{\"id\":\"165\",\"title\":\"WIND ENERGY SCIENCE, ENGINEERING AND POL\",\"abbreviation\":\"WESEP\"},{\"id\":\"166\",\"title\":\"WOMEN'S AND GENDER STUDIES\",\"abbreviation\":\"WGS\"},{\"id\":\"167\",\"title\":\"WOMEN'S STUDIES\",\"abbreviation\":\"W S\"},{\"id\":\"168\",\"title\":\"WORLD LANGUAGES AND CULTURES\",\"abbreviation\":\"WLC\"},{\"id\":\"169\",\"title\":\"YOUTH\",\"abbreviation\":\"YTH\"}]");
    private DatabaseConnection databaseConnection;
    private ArrayList<Department> departments;

    public static void main(String args[]) {
        CourseScraper courseScraper = new CourseScraper();
        courseScraper.getAll();
    }

    private CourseScraper() {
        System.out.println(departmentsJSON.toString());
        databaseConnection = new DatabaseConnection();
        departments = new ArrayList<>();
    }

    private void getAll() {
        getDepartments();
        getCourses();
        databaseConnection.closeConnection();
    }

    private void getDepartments() {
        for (int i = 0; i < departmentsJSON.length(); i++) {
            JSONObject departmentJSON = departmentsJSON.getJSONObject(i);
            Department department = new Department(
                    i + 1,
                    departmentJSON.getString("title").replaceAll("'", "''"),
                    departmentJSON.getString("abbreviation")
            );
            departments.add(department);
            databaseConnection.exportDepartment(department);
        }
    }

    private void getCourses() {
        try {
            int courseId = 1;
            URL url = new URL(API_URL);
            JSONObject jsonObject = buildPostObject();
            for (Department department : departments) {
                jsonObject.put("selectedDepartment", department.getAbbreviation());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                byte[] out = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                connection.setFixedLengthStreamingMode(length);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                connection.connect();
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(out);
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONArray coursesJSON = new JSONObject(response.toString()).getJSONArray("response");
                for (int i = 0; i < coursesJSON.length(); i++) {
                    JSONObject courseJSON = coursesJSON.getJSONObject(i);
                    JSONArray sections = courseJSON.getJSONArray("sections");
                    if (sections.length() > 0) {
                        Course course = new Course(
                                courseId++,
                                courseJSON.getString("classTitle").replaceAll("'", "''"),
                                courseJSON.getString("classNumber"),
                                department,
                                sections.getJSONObject(0).getInt("creditLow"),
                                0);
                        databaseConnection.exportCourse(course);
                        for (int j = 0; j < sections.length(); j++) {
                            JSONObject sectionJSON = sections.getJSONObject(j);
                            int classSize = sectionJSON.getInt("openseats");
                            if (classSize == 999 || classSize < 1) {
                                continue;
                            }

                            Section section = new Section(
                                    sectionJSON.getString("sectionID"),
                                    course,
                                    classSize);
                            databaseConnection.exportSection(section);
                        }
                    }
                }
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private JSONObject buildPostObject() {
        JSONObject semesters = new JSONObject("{\"semesters\":[{\"id\":2,\"code\":\"S\",\"year\":\"2018\",\"preliminary\":\"\",\"current\":\"Y\",\"lastChanged\":1518142992989,\"semNum\":\"1\",\"semesterTitle\":\"Spring 2018\",\"formattedStartDateTime\":\"Fri Feb 09 02:23:12 UTC 2018\"},{\"id\":3,\"code\":\"1\",\"year\":\"2018\",\"preliminary\":\"\",\"current\":\"N\",\"lastChanged\":1518143014562,\"semNum\":\"3\",\"semesterTitle\":\"Summer 2018\",\"formattedStartDateTime\":\"Fri Feb 09 02:23:34 UTC 2018\"},{\"id\":1,\"code\":\"F\",\"year\":\"2018\",\"preliminary\":\"Y\",\"current\":\"N\",\"lastChanged\":1518142962749,\"semNum\":\"5\",\"semesterTitle\":\"Tentative Fall 2018\",\"formattedStartDateTime\":\"Fri Feb 09 02:22:42 UTC 2018\"}],\"formDefaults\":{\"courseCredits\":{\"0.0\":\"0.0\",\"0.5\":\"0.5\",\"1.0\":\"1.0\",\"2.0\":\"2.0\",\"3.0\":\"3.0\",\"4.0\":\"4.0\",\"5.0\":\"5.0\",\"6.0\":\"6.0\"},\"courseLevels\":{\"100\":\"100 Level Courses\",\"200\":\"200 Level Courses\",\"300\":\"300 Level Courses\",\"400\":\"400 Level Courses\",\"500\":\"500 Level Courses\",\"600\":\"600 Level Courses\",\"000\":\"Developmental Courses\"},\"courseRequirements\":{\"N\":\"None\",\"D\":\"U.S. Diversity\",\"I\":\"International Perspectives\"},\"courseTypes\":{\"All\":\"All\",\"WWW\":\"Online\",\"EX\":\"Experimental\",\"X\":\"Distance Education\",\"SAT\":\"Satisfactory/Fail\"}}}");
        semesters.put("departments", departmentsJSON);
        JSONObject jsonObject = new JSONObject("{\"defSem\":2,\"selectedTerm\":\"1\",\"startTime\":\"\",\"stopTime\":\"\"}");
        jsonObject.put("semesters", semesters);
        return jsonObject;
    }
}
