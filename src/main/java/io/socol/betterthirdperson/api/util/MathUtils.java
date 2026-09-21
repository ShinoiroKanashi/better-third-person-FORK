package io.socol.betterthirdperson.api.util;

public class MathUtils {
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double easeInExpo(double x) {
        return x == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * x - 10.0);
    }
}
