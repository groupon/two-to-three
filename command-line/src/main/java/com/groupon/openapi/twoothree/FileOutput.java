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

import java.io.File;
import java.io.IOException;

import io.swagger.models.Swagger;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
/**
 * Output the swagger or openapi 3.0 into a file
 *
 * @author Alex Campelo (acampelo at groupon dot com)
 * @since 1.0.0
 */
public class FileOutput {

    private File output;

    public FileOutput(File output) {
        this.output = output;
    }

    public void fileOutputYaml(Swagger swagger) throws IOException {
        Yaml.pretty().writeValue(output, swagger);
    }

    public void fileOutputYaml(OpenAPI openAPI) throws IOException {
        Yaml.pretty().writeValue(output, openAPI);
    }
}
