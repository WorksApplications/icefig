/*
 * Copyright (C) 2015 The Fig Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.worksap.icefig.lang;

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
