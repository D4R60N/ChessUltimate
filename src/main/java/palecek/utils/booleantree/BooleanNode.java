package palecek.utils.booleantree;

import java.util.Map;

public interface BooleanNode {

    public boolean evaluate(Map<String, Object> context);

}
