package kr.ac.smu.cs.shalendar_java;

public class Global {
    private static int cid = 0;

    public static int getCid() {
        return cid;
    }

    public static void setCid(int cid) {
        Global.cid = cid;
    }
}
