package AllUtil.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class MapConvert {
    /**
     * Map<Object,Object>转为Map<String,String>
     * @param inputMap
     * @return
     */
    public static Map<String, String> convertMap(Map<Object, Object> inputMap) {
        Map<String, String> outputMap = new HashMap<>();

        for (Map.Entry<Object, Object> entry : inputMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            outputMap.put(key, value);
        }

        return outputMap;
    }
}
