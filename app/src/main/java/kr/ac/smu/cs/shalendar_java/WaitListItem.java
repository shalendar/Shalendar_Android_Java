package kr.ac.smu.cs.shalendar_java;

public class WaitListItem {

    String waitPeoplePic;
    String emailID;
    String calendarName;
    String invitedName;
    String inviteName;

    public WaitListItem(String waitPeoplePic, String emailID, String calendarName, String invitedName, String inviteName) {
        this.waitPeoplePic = waitPeoplePic;
        this.emailID = emailID;
        this.calendarName = calendarName;
        this.invitedName = invitedName;
        this.inviteName = inviteName;
    }

    public String getWaitPeoplePic() {
        return this.waitPeoplePic;
    }

    public void setWaitPeoplePic(String waitPeoplePic) {
        this.waitPeoplePic = waitPeoplePic;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public String getInvitedName() {
        return invitedName;
    }

    public void setInvitedName(String invitedName) {
        this.invitedName = invitedName;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }
}
