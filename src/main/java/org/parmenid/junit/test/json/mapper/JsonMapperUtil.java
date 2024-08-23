package org.parmenid.junit.test.json.mapper;

import java.util.regex.Pattern;
public class JsonMapperUtil {
    private JsonMapperUtil() {
    }
    private static final String SPLIT_OBJECTS_ARRAY_PATTERN = "(},)";
    private static final String SPLIT_PRIMITIVES_ARRAY_PATTERN = ",";
    public static boolean isArray(String json) {
        Pattern bracketPattern = Pattern.compile("(\\[|\\])");
        int length = json.length();
        String firstJsonChar = String.valueOf(json.charAt(0));
        String lastJsonChar = String.valueOf(json.charAt(length - 1));
        return bracketPattern.matcher(firstJsonChar).find() && bracketPattern.matcher(lastJsonChar).find();
    }

    public static boolean isArrayOrCollection(Class<?> target) {
        if (target.isArray()) {
            return true;
        }
        Class<?>[] interfaces = target.getInterfaces();
        for (Class<?> c : interfaces) {
            if (c.getSimpleName().contains("Collection")) {
                return true;
            }
        }
        return false;
    }
    public static boolean resourceIsFile(String[] resources) {
        String filePattern = "(/?).+\\.(json)";
        return resources[0].matches(filePattern);
    }

    public static String[] extractJsonObjects(String json) {
        String[] objects = json.split(SPLIT_OBJECTS_ARRAY_PATTERN);
        int firstIndex = 0, lastIndex = objects.length - 1;
        objects[firstIndex] = objects[firstIndex].replace("[", "");
        objects[lastIndex] = objects[lastIndex].replace("]", "");
        return objects;
    }

    public static String[] extractJsonPrimitives(String json) {
        String[] primitives = json.split(SPLIT_PRIMITIVES_ARRAY_PATTERN);
        int firstIndex = 0, lastIndex = primitives.length - 1;
        primitives[firstIndex] = primitives[firstIndex].replace("[", "");
        primitives[lastIndex] = primitives[lastIndex].replace("]", "");
        return primitives;
    }

    public static boolean isObject(String json) {
        String jsonObjectPattern = "(\\{.\\})";
        return json.matches(jsonObjectPattern);
    }
}
