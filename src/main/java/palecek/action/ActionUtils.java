package palecek.action;

import palecek.utils.Separators;

public class ActionUtils {
    public static Action parseAction(String actionString) {
        String[] parts = actionString.split(Separators.TYPE_SEPARATOR);

        switch (parts[0]) {
            case "CREATE":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid CREATE action string: " + actionString);
                }
                if (parts[1].equals("FROM") || parts[1].equals("TO")) {
                    return new CreatePieceFromMoveAction(actionString);
                } else {
                    return new CreatePieceFromSpecialMoveAction(actionString);
                }
            default:
                throw new IllegalArgumentException("Unknown action type: " + actionString);
        }
    }
}
