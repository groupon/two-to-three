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

import java.math.BigDecimal;
import java.util.Map;

import io.swagger.models.Swagger;
import io.swagger.parser.util.SwaggerDeserializationResult;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.converter.SwaggerConverter;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import com.groupon.openapi.twoothree.util.CollectionUtil;


/**
 * Main Class that perform the transformation from swagger to OpenAPI
 *
 * @author Alex Campelo (acampelo at groupon dot com)
 * @since 1.0.0
 */
public class ConvertToOpenAPI {

    /**
     * @param swagger input swagger object
     * @return the open api 3.0 converted
     */
    public OpenAPI convert(Swagger swagger) {
        SwaggerConverter converter = new SwaggerConverter();
        SwaggerDeserializationResult deserializationResult = new SwaggerDeserializationResult();
        deserializationResult.setSwagger(swagger);
        SwaggerParseResult swaggerParseResult = converter.convert(deserializationResult);

        OpenAPI openAPI = swaggerParseResult.getOpenAPI();
        removeUUIDParams(openAPI);
        fixSchemas(openAPI);
        return openAPI;
    }

    private void removeUUIDParams(OpenAPI openAPI) {
        openAPI.getPaths()
                .forEach((s, pathItem) -> pathItem.readOperations()
                        .forEach(operation -> operation.getParameters().forEach(parameter -> {
                            if (parameter.getSchema() != null && "uuid".equalsIgnoreCase(parameter.getSchema().getFormat())) {
                                parameter.getSchema().setFormat(null);
                                parameter.getSchema().setType("string");
                            }
                        })));
    }

    @SuppressWarnings("unchecked")
    private void fixSchemas(OpenAPI openAPI) {
        if (openAPI.getComponents() == null || CollectionUtil.isEmpty(openAPI.getComponents().getSchemas())) {
            return;
        }
        openAPI.getComponents().getSchemas().forEach((s, schema) -> {
            if (schema.getProperties() != null) {
                Map<String, Schema<?>> properties = schema.getProperties();
                properties.forEach((o, schemaItem) -> {
                    if (schemaItem instanceof IntegerSchema) {
                        if (schemaItem.getMinimum() != null) {
                            schemaItem.setMinimum(BigDecimal.valueOf(schemaItem.getMinimum().intValue()));
                        }
                        if (schemaItem.getMaximum() != null) {
                            schemaItem.setMaximum(BigDecimal.valueOf(schemaItem.getMaximum().intValue()));
                        }
                    }
                    if (schemaItem instanceof NumberSchema) {
                        if (schemaItem.getMinimum() != null) {
                            schemaItem.setMinimum(BigDecimal.valueOf(schemaItem.getMinimum().doubleValue()));
                        }
                        if (schemaItem.getMaximum() != null) {
                            schemaItem.setMaximum(BigDecimal.valueOf(schemaItem.getMaximum().doubleValue()));
                        }
                    }
                });
            }
        });
    }
}
