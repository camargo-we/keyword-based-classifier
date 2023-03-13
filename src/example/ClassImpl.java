package example;

import classifier.Class;

public class ClassImpl implements Class {
    private String id, name, keywords;

    public ClassImpl(String id, String name, String keywords) {
        this.id = id;
        this.name = name;
        this.keywords = keywords;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKeywords() {
        return keywords;
    }

}
