package AllUtil.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringChange {
    public static String splitBySymbol(String sentence,String symbol){
        String[] parts = sentence.split(symbol);
        String result = "";
        if (parts.length >= 2) {
            result = parts[1];
        }
        return result;
    }

    /**
     * 字符串“[11,22,12]”中的数字解析出来放到List里
     */
    public static List<Integer> stringToList(String str){
        List<Integer> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String numberStr = matcher.group();
            int number = Integer.parseInt(numberStr);
            numbers.add(number);
        }
        return numbers;
    }

    /**
     * 把字符串里的数字取出来
     */
    public static Integer stringGetNumber(String str){
        String regex = "\\d+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        int number=0;
        while (matcher.find()) {
            number += Integer.parseInt(matcher.group());
        }
        return number;
    }
}
