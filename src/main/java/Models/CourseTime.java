package Models;

import java.sql.Time;

public class CourseTime {
    public enum MeetingDays {
        T,
        TH,
        TTH,
        M,
        W,
        F,
        MW,
        MF,
        WF,
        MWF
    }

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
        HOURHALF1230,
        HOURHALF2,
        HOURHALF330
    }

    private MeetingDays meetingDays;
    private Time startTime;
    private Time endTime;

    public CourseTime(MeetingDays meetingDays, Time startTime, Time endTime) {
        this.meetingDays = meetingDays;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public MeetingDays getMeetingDays() {
        return meetingDays;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public boolean timeOverlap(CourseTime courseTime) {
        switch (courseTime.getMeetingDays()) {
            case M:
            case W:
            case F:
            case MW:
            case MF:
            case WF:
            case MWF:
                if (this.getMeetingDays() == MeetingDays.TTH
                        || this.getMeetingDays() == MeetingDays.T
                        || this.getMeetingDays() == MeetingDays.TH) {
                    return false;
                }
                break;

            case T:
            case TH:
            case TTH:
                if (this.getMeetingDays() != MeetingDays.TTH
                        && this.getMeetingDays() != MeetingDays.T
                        && this.getMeetingDays() != MeetingDays.TH) {
                    return false;
                }
                break;

            default: // Should not get here
                break;
        }

        return !this.endTime.before(courseTime.getStartTime()) && !this.startTime.after(courseTime.getEndTime());

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!CourseTime.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final CourseTime courseTime = (CourseTime) obj;

        return courseTime.getMeetingDays() == this.getMeetingDays() &&
                courseTime.getStartTime().equals(this.getStartTime()) &&
                courseTime.getEndTime().equals(this.getEndTime());
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
                MeetingTime.HOURHALF1230.ordinal(),
                MeetingTime.HOURHALF2.ordinal(),
                MeetingTime.HOURHALF330.ordinal()
        };
    }

}
