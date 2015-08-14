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

package com.worksap.fig.lang;

import java.util.Map;

/**
 * Factory class for construct Hash and MutableHash
 */
public class Hashes {
    private Hashes() {

    }

    public static <K, V> Hash<K, V> newHash() {
        return new HashImpl<>();
    }

    public static <K, V> Hash<K, V> newHash(Map<K, V> map) {
        return new HashImpl<>(map);
    }

    public static <K, V> MutableHash<K, V> newMutableHash() {
        return new HashImpl<>();
    }

    public static <K, V> MutableHash<K, V> newMutableHash(Map<K, V> map) {
        return new HashImpl<>(map);
    }
}
