package URL;

public enum Website {
    AMAZON;

    public static Website getSite(String url) {
        if (url.contains("amazon")) {
            return AMAZON;
        }
        return AMAZON;
    }
}
