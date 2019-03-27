/**
 * Copyright 2018 Groupon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.groupon.openapi.twoothree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter the swagger files based on the suffix and exclude
 *
 * @author Alex Campelo (acampelo at groupon dot com)
 * @since 1.0.0
 */
public class FileFilter {
    private String filePath;

    public FileFilter(String path) {
        this.filePath = path;
    }

    public List<String> filter(List<String> exclude, String includeSuffix) throws IOException {
        return Files.find(Paths.get(filePath), Integer.MAX_VALUE,
                (path, basicFileAttributes) -> {
                    String absolutePath = path.toAbsolutePath().toString();
                    for (String toExclude : exclude) {
                        if (absolutePath.contains(toExclude)) {
                            return false;
                        }
                    }
                    return absolutePath.endsWith(includeSuffix);
                })
                .map(Path::toString)
                .collect(Collectors.toList());
    }
}
