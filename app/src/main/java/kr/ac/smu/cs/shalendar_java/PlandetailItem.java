package kr.ac.smu.cs.shalendar_java;

public class PlandetailItem {

    String reply_picture;
    String reply_name;
    String reply_content;
    String reply_date;
    int commentNum;


    public PlandetailItem( String reply_name, String reply_content, String reply_date, int commentNum) {
        this.reply_name = reply_name;
        this.reply_content = reply_content;
        this.reply_date = reply_date;
        this.commentNum = commentNum;
    }

    public PlandetailItem(String reply_picture, String reply_name, String reply_date, String reply_content) {
        this.reply_picture = reply_picture;
        this.reply_name = reply_name;
        this.reply_content = reply_content;
        this.reply_date = reply_date;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getReply_picture() {
        return reply_picture;
    }

    public void setReply_picture(String reply_picture) {
        this.reply_picture = reply_picture;
    }

    public String getReply_name() {
        return reply_name;
    }

    public void setReply_name(String reply_name) {
        this.reply_name = reply_name;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReply_date() {
        return reply_date;
    }

    public void setReply_date(String reply_date) {
        this.reply_date = reply_date;
    }
}
