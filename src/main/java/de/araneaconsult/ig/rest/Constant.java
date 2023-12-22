package de.araneaconsult.ig.rest;

public final class Constant {
    public enum Match {
        START("START"),
        ANY("ANY"),
        EXACT("EXACT");

        private final String value;

        private Match(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
