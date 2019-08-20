package kr.ac.smu.cs.shalendar_java;

/*

  공유달력의 멤버 초대 화면에서
  Email RecyclerView를 하기 위한 Email클라스
 */
public class UserEmail {

    private String user_Email;
    private Boolean is_checked;

    public UserEmail(String user_Email) {
        this.user_Email = user_Email;
    }

    public UserEmail(String user_Email, Boolean is_checked) {
        this.user_Email = user_Email;
        this.is_checked = is_checked;
    }

    public void setUserEmail(String user_Email) {
        this.user_Email = user_Email;
    }

    public String getUserEmail() {
        return this.user_Email;
    }

    public void setIs_checked(Boolean is_checked) {
        this.is_checked = is_checked;
    }

    public Boolean getIs_checked() {
        return this.is_checked;
    }
}
