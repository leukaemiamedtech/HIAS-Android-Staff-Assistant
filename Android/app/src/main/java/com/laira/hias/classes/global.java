package com.laira.hias.classes;

public class global {

    private static String Server;
    private static String LoginEndpoint = "/Hospital/Staff/API/Applications/User/Login";
    private static String NluEndpoint = "/Hospital/Staff/API/Applications/NLU/Infer";
    private static String LOG_TAG = "HIAS";
    private static int UID;
    private static int AID;
    private static String APB;
    private static String APV;
    private static String Uname;
    private static String Upass;

    public static String getTag() { return LOG_TAG; }

    public static void setServer(String server) {
        Server = server;
    }
    public static String getServer() { return Server; }

    public static String getLoginEndpoint() { return LoginEndpoint; }

    public static String getNluEndpoint() { return NluEndpoint; }

    public static void setUID(int UIDS) { UID = UIDS; }
    public static int getUID() { return UID; }

    public static void setUname(String UnameS) { Uname = UnameS; }
    public static String getUname() { return Uname; }

    public static void setUpass(String UpassS) { Upass = UpassS; }
    public static String getUpass() { return Upass; }

    public static void setAID(int AIDs) { AID = AIDs; }
    public static int getAID() { return AID; }

    public static void setAPB(String APBs) { APB = APBs; }
    public static String getAPB() { return APB; }

    public static void setAPV(String APVs) { APV = APVs; }
    public static String getAPV() { return APV; }
}
