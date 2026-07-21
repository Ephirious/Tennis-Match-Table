package com.ephirious.model.value.match;

public enum StandardPointState {
    LOVE("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40");

    private final String points;

    StandardPointState(String points) {
        this.points = points;
    }

    public String value() {
        return points;
    }

    public StandardPointState next() {
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> throw new IllegalStateException("");
        };
    }
}