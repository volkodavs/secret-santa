package com.sergeyvolkodav.secretsanta.model;

public enum RelationType {

    PARENT, CHILD, SPOUSE, SIBLING;

    private RelationType opposite;

    static {
        PARENT.opposite = CHILD;
        CHILD.opposite = PARENT;
        SPOUSE.opposite = SPOUSE;
        SIBLING.opposite = SIBLING;
    }

    public RelationType getOppositeRelation() {
        return opposite;
    }
}
