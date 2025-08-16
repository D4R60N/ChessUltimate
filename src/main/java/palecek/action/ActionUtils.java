package palecek.action;

import palecek.utils.Separators;

public class ActionUtils {
    public static IAction parseAction(String actionString) {
        String[] parts = actionString.split(Separators.TYPE_SEPARATOR);

        switch (parts[0]) {
            case "CREATE":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid CREATE action string: " + actionString);
                }
                if (parts[1].equals("FROM") || parts[1].equals("TO")) {
                    return new CreatePieceFromMoveIAction(actionString);
                } else {
                    return new CreatePieceFromSpecialMoveIAction(actionString);
                }
            case "REMOVE":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid REMOVE action string: " + actionString);
                }
                if (parts[1].equals("FROM") || parts[1].equals("TO")) {
                    return new RemovePieceFromMoveIAction(actionString);
                } else {
                    return new RemovePieceFromSpecialMoveIAction(actionString);
                }
            default:
                throw new IllegalArgumentException("Unknown action type: " + actionString);
        }
    }
}
