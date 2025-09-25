package edu.ccrm.domain;

import java.util.Objects;

public final class CourseCode {
    private final String prefix;
    private final String number;

    public CourseCode(String prefix, String number) {
        this.prefix = Objects.requireNonNull(prefix, "Course prefix cannot be null");
        this.number = Objects.requireNonNull(number, "Course number cannot be null");

        if (prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Course prefix cannot be empty");
        }
        if (number.trim().isEmpty()) {
            throw new IllegalArgumentException("Course number cannot be empty");
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseCode that = (CourseCode) o;
        return prefix.equals(that.prefix) && number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, number);
    }

    @Override
    public String toString() {
        return prefix + number;
    }
}