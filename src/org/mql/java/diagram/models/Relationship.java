package org.mql.java.diagram.models;

public class Relationship {
    private String from;
    private String to;
    private String relationshipType;

    public Relationship(String from, String to, String relationshipType) {
        this.from = from;
        this.to = to;
        this.relationshipType = relationshipType;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    @Override
    public String toString() {
        return from + " -- " + to + " : " + relationshipType;
    }
}
