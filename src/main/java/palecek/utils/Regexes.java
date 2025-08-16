package palecek.utils;

public class Regexes {
    public static String SPECIAL_MOVE_REGEX = "^(?:F|R)(?:f|c|l).*";
    public static String MOVE_REGEX = "^\\d+:[fblr].*";
    public static String PIECE_REGEX = "^[a-z]+$";
    public static String PAYLOAD_REGEX = "^P:.+$";
}
