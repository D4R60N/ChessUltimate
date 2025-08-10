package palecek.utils;

public enum SpaceType {
    FROM("FROM"),
    TO("TO");

    private final String value;
    SpaceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SpaceType getFromString(String value) {
        for (SpaceType spaceType : SpaceType.values()) {
            if (spaceType.value.equalsIgnoreCase(value)) {
                return spaceType;
            }
        }
        throw new IllegalArgumentException("No Space found for value: " + value);
    }
}
