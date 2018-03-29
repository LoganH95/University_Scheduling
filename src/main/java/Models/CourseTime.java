package Models;

public class CourseTime {

    public enum MeetingTime {
        HOUR8,
        HOUR9,
        HOUR10,
        HOUR11,
        HOUR12,
        HOUR1,
        HOUR2,
        HOUR3,
        HOUR4,
        HOURHALF8,
        HOURHALF930,
        HOURHALF11,
        HOURHALF1240,
        HOURHALF210,
        HOURHALF340
    }

    private MeetingTime meetingTime;

    public CourseTime(MeetingTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public CourseTime(int meetingTime) {
        this.meetingTime = MeetingTime.values()[meetingTime];
    }

    public MeetingTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(MeetingTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public boolean timeOverlap(CourseTime courseTime) {
        return this.meetingTime == courseTime.meetingTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!CourseTime.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final CourseTime courseTime = (CourseTime) obj;

        return courseTime.getMeetingTime() == this.getMeetingTime();
    }

    public static int[] meetingTimes() {
        return new int[] {
                MeetingTime.HOUR8.ordinal(),
                MeetingTime.HOUR9.ordinal(),
                MeetingTime.HOUR10.ordinal(),
                MeetingTime.HOUR11.ordinal(),
                MeetingTime.HOUR12.ordinal(),
                MeetingTime.HOUR1.ordinal(),
                MeetingTime.HOUR2.ordinal(),
                MeetingTime.HOUR3.ordinal(),
                MeetingTime.HOUR4.ordinal(),
                MeetingTime.HOURHALF8.ordinal(),
                MeetingTime.HOURHALF930.ordinal(),
                MeetingTime.HOURHALF11.ordinal(),
                MeetingTime.HOURHALF1240.ordinal(),
                MeetingTime.HOURHALF210.ordinal(),
                MeetingTime.HOURHALF340.ordinal()
        };
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (meetingTime.ordinal() < 9) {
            stringBuilder.append("Monday, Wednesday, Friday ");
        } else {
            stringBuilder.append("Tuesday, Thursday ");
        }
        stringBuilder.append(meetingTimeToString());

        return stringBuilder.toString();
    }

    private String meetingTimeToString() {
        switch (meetingTime) {
            case HOUR8:
                return "8:00 AM - 8:50 AM";
            case HOUR9:
                return "9:00 AM - 9:50 AM";
            case HOUR10:
                return "10:00 AM - 10:50 AM";
            case HOUR11:
                return "11:00 AM - 11:50 AM";
            case HOUR12:
                return "12:10 PM - 1:00 PM";
            case HOUR1:
                return "1:10 PM - 2:00 PM";
            case HOUR2:
                return "2:10 PM - 3:00 PM";
            case HOUR3:
                return "3:10 PM - 4:00 PM";
            case HOUR4:
                return "4:10 PM - 5:00 PM";
            case HOURHALF8:
                return "8:00 AM - 9:15 AM";
            case HOURHALF930:
                return "9:30 AM - 10:45 AM";
            case HOURHALF11:
                return "11:00 AM - 12:15 PM";
            case HOURHALF1240:
                return "12:40 PM - 1:55 PM";
            case HOURHALF210:
                return "2:10 PM - 3:25 PM";
            case HOURHALF340:
                return "3:40 PM - 4:55 PM";
            default:
                return "";

        }
    }
}
