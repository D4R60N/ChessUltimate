package palecek.entity;

public class Space {
    private String head;
    private String tail;

    public Space(String head, String tail) {
        this.head = head;
        this.tail = tail;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public int getPlayer() {
        if (head == null || head.isEmpty() || head.length() < 2) {
            return -1;
        }
        String[] split = head.split("\\.");
        if (split.length <= 1) {
            return -1;
        }
        String player = split[0].trim();
        if (player.charAt(0) == 'p') {
            try {
                return Integer.parseInt(player.substring(1));
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        if (head == null) {
            return tail;
        }

        if (tail == null) {
            return head;
        }

        return head + " - " + tail;
    }
}
