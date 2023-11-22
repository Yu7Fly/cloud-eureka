package AllUtil.StringUtils;


public class StringChange {
    public static String splitBySymbol(String sentence,String symbol){
        String[] parts = sentence.split(symbol);
        String result = "";
        if (parts.length >= 2) {
            result = parts[1];
        }
        return result;
    }
}
