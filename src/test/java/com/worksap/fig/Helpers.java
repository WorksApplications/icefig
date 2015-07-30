package com.worksap.fig;

/**
 * Created by liuyang on 7/30/15.
 */
public class Helpers {

    public static void assertThrows(Class<? extends Exception> exception, Runnable action) {
        try {
            action.run();
            throw new AssertionError("Expected exception: " + exception.getTypeName() + ", but no exception thrown");
        } catch (Exception e) {
            if (!e.getClass().equals(exception)) {
                throw new AssertionError("Expected exception: " + exception.getTypeName() + ", but was " + e.getClass().getTypeName());
            }
        }
    }
}
