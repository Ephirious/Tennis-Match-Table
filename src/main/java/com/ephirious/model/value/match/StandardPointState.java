package com.ephirious.model.value.match;

public enum StandardPointState {
    LOVE,
    FIFTEEN,
    THIRTY,
    FORTY;

    public StandardPointState next() {
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> throw new IllegalStateException("");
        };
    }
}