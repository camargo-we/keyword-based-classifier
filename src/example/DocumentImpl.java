package example;

import classifier.Document;

public class DocumentImpl implements Document {
    private final String title;
    private final String content;

    DocumentImpl(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "---------------------------- [ D O C U M E N T ] -------------------------------" +
                String.format("\nTITLE: %s", this.getTitle()) +
                String.format("\nTEXT CONTENT: \n%s", this.getContent()) +
                "\n--------------------------------------------------------------------------------";
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getContent() {
        return content;
    }
}
