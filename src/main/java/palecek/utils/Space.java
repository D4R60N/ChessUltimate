package palecek.utils;

public enum Space {
    FROM("FROM"),
    TO("TO");

    private final String value;
    Space(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Space getFromString(String value) {
        for (Space space : Space.values()) {
            if (space.value.equalsIgnoreCase(value)) {
                return space;
            }
        }
        throw new IllegalArgumentException("No Space found for value: " + value);
    }
}
