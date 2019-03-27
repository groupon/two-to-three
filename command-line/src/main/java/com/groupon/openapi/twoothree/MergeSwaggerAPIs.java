/**
 * Copyright 2018 Groupon.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.groupon.openapi.twoothree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;

import com.groupon.openapi.twoothree.util.CollectionUtil;

/**
 * Merge several swagger files into a single source
 *
 * @author Alex Campelo (acampelo at groupon dot com)
 * @since 1.0.0
 */
public class MergeSwaggerAPIs {

    private static final Swagger20Parser SWAGGER_PARSER = new Swagger20Parser();
    private final Swagger mergedSwagger;
    private final List<String> tagsToRemove = new ArrayList<>();
    private boolean removeSecurity;

    public MergeSwaggerAPIs(String baseSwagger, List<String> tagsToRemove) throws IOException {
        this(SWAGGER_PARSER.read(baseSwagger, Collections.emptyList()), tagsToRemove);
    }

    public void setRemoveSecurity(boolean removeSecurity) {
        this.removeSecurity = removeSecurity;
    }

    public MergeSwaggerAPIs(Swagger mergedSwagger, List<String> givenTagsToRemove) {
        this.mergedSwagger = mergedSwagger;
        this.tagsToRemove.addAll(givenTagsToRemove);
    }

    /**
     * merge given swagger objects into a single swagger object
     * @param swaggers the swagger objects
     * @return
     */
    public Swagger merge(Swagger... swaggers) {
        for (Swagger swagger : swaggers) {
            CollectionUtil.emptyIfNull(swagger.getTags())
                    .forEach(tag -> {
                        if (!tagsToRemove.contains(tag.getName())) {
                            mergedSwagger.addTag(tag);
                        }
                    });
            if (!removeSecurity) {
                CollectionUtil.emptyIfNull(swagger.getSecurityDefinitions())
                        .forEach(mergedSwagger::addSecurityDefinition);
                CollectionUtil.emptyIfNull(swagger.getSecurity())
                        .forEach(mergedSwagger::addSecurity);
            }
            CollectionUtil.emptyIfNull(swagger.getConsumes())
                    .forEach(mergedSwagger::addConsumes);
            CollectionUtil.emptyIfNull(swagger.getProduces())
                    .forEach(mergedSwagger::addProduces);
            CollectionUtil.emptyIfNull(swagger.getDefinitions())
                    .forEach(mergedSwagger::addDefinition);
            CollectionUtil.emptyIfNull(swagger.getParameters())
                    .forEach(mergedSwagger::addParameter);
            addPath(swagger);
        }
        return mergedSwagger;
    }

    private void addPath(Swagger swagger1) {
        Map<String, Path> pathMap = CollectionUtil.emptyIfNull(swagger1.getPaths());

        pathMap.forEach((key, path) -> {
            if (shouldRemove(path.getGet())) {
                path.get(null);
            }
            if (shouldRemove(path.getPost())) {
                path.post(null);
            }
            if (shouldRemove(path.getPut())) {
                path.put(null);
            }
            if (shouldRemove(path.getDelete())) {
                path.delete(null);
            }
            if (shouldRemove(path.getPatch())) {
                path.patch(null);
            }
            removeSecurity(path.getGet());
            removeSecurity(path.getPost());
            removeSecurity(path.getPut());
            removeSecurity(path.getDelete());
            removeSecurity(path.getPatch());
        });

        pathMap.forEach((key, value) -> {
            if (!value.getOperations().isEmpty()) {
                mergedSwagger.path(key, value);
            }
        });
    }

    private boolean shouldRemove(@Nullable Operation operation) {
        return operation != null && CollectionUtil.isNotEmpty(operation.getTags()) &&  !Collections.disjoint(operation.getTags(), tagsToRemove);
    }

    private void removeSecurity(@Nullable Operation operation) {
        if (removeSecurity && operation != null) {
            operation.setSecurity(null);
        }
    }

    /**
     * Merge swagger file locations into a single swagger
     * @param swaggerLocations all the swagger locations to merge
     * @return the merged swagger
     * @throws IOException
     */
    public Swagger merge(String... swaggerLocations) throws IOException {
        Swagger[] swaggers = new Swagger[swaggerLocations.length];
        int i = 0;
        for (String swaggerLocation : swaggerLocations) {
            swaggers[i++] = SWAGGER_PARSER.read(swaggerLocation, Collections.emptyList());
        }
        return merge(swaggers);
    }
}
