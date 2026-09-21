package io.socol.betterthirdperson.api.util;

import io.socol.betterthirdperson.api.util.MathUtils;

public class AngleUtils {
    public static float smoothAngle(float partialTicks, float prevAngle, float currentAngle) {
        double anglesDiff = ((double)currentAngle - (double)prevAngle + 180.0) % 360.0 - 180.0;
        return (float)((double)prevAngle + (double)partialTicks * (anglesDiff < -180.0 ? anglesDiff + 360.0 : anglesDiff));
    }

    public static float wrapAngle(float prevAngle, float currentAngle) {
        double anglesDiff = ((double)currentAngle - (double)prevAngle + 180.0) % 360.0 - 180.0;
        return (float)((double)prevAngle + (anglesDiff < -180.0 ? anglesDiff + 360.0 : anglesDiff));
    }

    public static float getDelta(float prevAngle, float currentAngle) {
        double anglesDiff = ((double)currentAngle - (double)prevAngle + 180.0) % 360.0 - 180.0;
        return (float)(anglesDiff < -180.0 ? anglesDiff + 360.0 : anglesDiff);
    }

    public static float normalize(float angle) {
        float result = angle % 360.0f;
        if (result >= 180.0f) {
            result -= 360.0f;
        }
        if (result < -180.0f) {
            result += 360.0f;
        }
        return result;
    }

    public static float stepAngle(float partialTicks, float maxAngle, float prevAngle, float currentAngle) {
        double deltaAngle = partialTicks * AngleUtils.getDelta(prevAngle, currentAngle);
        return prevAngle + MathUtils.clamp((float)deltaAngle, -maxAngle, maxAngle);
    }

    public static boolean equals(float a1, float a2) {
        return Math.abs(AngleUtils.getDelta(a1, a2)) < 0.001f;
    }
}
