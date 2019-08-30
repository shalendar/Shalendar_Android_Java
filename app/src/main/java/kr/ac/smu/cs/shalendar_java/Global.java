package kr.ac.smu.cs.shalendar_java;

public class Global {
    private static int sid = 0;

    public static int getSid() {
        return sid;
    }

    public static void setSid(int sid) {
        Global.sid = sid;
    }
}
