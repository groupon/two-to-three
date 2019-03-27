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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.swagger.models.Swagger;
import io.swagger.v3.oas.models.OpenAPI;
import picocli.CommandLine;

/**
 * Main Class that perform the conversion
 *
 * @author Alex Campelo (acampelo at groupon dot com)
 * @since 1.0.0
 */
@CommandLine.Command(description = "concerts a swagger file to open api", name = "two-o-three",
        mixinStandardHelpOptions = true)
public final class ExecuteConvertion implements Callable<Void> {

    @CommandLine.Parameters(index = "0", description = "The base swagger file to convert.")
    private File file;

    @CommandLine.Option(names = {"-o", "--output"}, description = "file to output the converted swagger")
    private String output;

    @CommandLine.Option(names = {"-s", "--swagger-path"}, description = "file to output the converted swagger")
    private String additionalSwaggerPath;

    @CommandLine.Option(names = {"-f", "--filter-swagger-suffix"}, description = "suffix to filter the swaggers to merge")
    private String filterSwaggerSuffix;

    @CommandLine.Option(names = {"-tx", "--tags-to-exclude"}, description = "tags to be excluded from the conversion, comma separated")
    private String[] tagsToExclude = new String[]{};

    @CommandLine.Option(names = {"-t", "--format"}, description = "output as openapi3 (default) or swagger/openapi2")
    private OpenAPIFormat openAPIFormat = OpenAPIFormat.openapi3;

    @CommandLine.Option(names = {"-e", "--filter-swagger"}, description = "swaggers to exclude")
    private String[] excludeSwaggerFilter = new String[]{};

    @CommandLine.Option(names = {"-rx", "--remove-security"}, description = "if it should rempove security")
    private boolean removeSecurity = false;

    private ExecuteConvertion(Builder builder) {
        file = builder.file;
        output = builder.output;
        additionalSwaggerPath = builder.additionalSwaggerPath;
        filterSwaggerSuffix = builder.filterSwaggerSuffix;
        tagsToExclude = builder.tagsToExclude;
        openAPIFormat = builder.openAPIFormat;
        excludeSwaggerFilter = builder.excludeSwaggerFilter;
        removeSecurity = builder.removeSecurity;
    }

    public static void main(String[] args) {
        CommandLine.call(new ExecuteConvertion(), args);
    }

    @Override
    public Void call() throws Exception {
        List<String> swaggers = Collections.emptyList();
        if (additionalSwaggerPath != null && !additionalSwaggerPath.trim().isEmpty()) {
            swaggers = new FileFilter(additionalSwaggerPath).filter(Arrays.asList(excludeSwaggerFilter), filterSwaggerSuffix);
        }

        MergeSwaggerAPIs swaggerMerger = new MergeSwaggerAPIs(file.getAbsolutePath(), Arrays.asList(tagsToExclude));
        swaggerMerger.setRemoveSecurity(removeSecurity);
        Swagger mergedSwagger = swaggerMerger
                .merge(swaggers.toArray(new String[]{}));

        FileOutput fileOutput = new FileOutput(new File(output));

        if (OpenAPIFormat.openapi2.equals(openAPIFormat)) {
            fileOutput.fileOutputYaml(mergedSwagger);
        } else {
            OpenAPI openAPI = new ConvertToOpenAPI().convert(mergedSwagger);
            fileOutput.fileOutputYaml(openAPI);
        }
        return null;
    }

    public ExecuteConvertion() {
    }

    /**
     * {@code ConvertArguments} builder static inner class.
     */
    public static final class Builder {
        private File file;
        private String output;
        private String additionalSwaggerPath;
        private String filterSwaggerSuffix;
        private String[] tagsToExclude;
        private OpenAPIFormat openAPIFormat;
        private String[] excludeSwaggerFilter;
        private boolean removeSecurity;

        /**
         * Sets the {@code file} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code file} to set
         * @return a reference to this Builder
         */
        public Builder setFile(File value) {
            file = value;
            return this;
        }

        /**
         * Sets the {@code output} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code output} to set
         * @return a reference to this Builder
         */
        public Builder setOutput(String value) {
            output = value;
            return this;
        }

        /**
         * Sets the {@code additionalSwaggerPath} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code additionalSwaggerPath} to set
         * @return a reference to this Builder
         */
        public Builder setAdditionalSwaggerPath(String value) {
            additionalSwaggerPath = value;
            return this;
        }

        /**
         * Sets the {@code filterSwaggerSuffix} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code filterSwaggerSuffix} to set
         * @return a reference to this Builder
         */
        public Builder setFilterSwaggerSuffix(String value) {
            filterSwaggerSuffix = value;
            return this;
        }

        /**
         * Sets the {@code tagsToExclude} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code tagsToExclude} to set
         * @return a reference to this Builder
         */
        public Builder setTagsToExclude(String[] value) {
            tagsToExclude = value;
            return this;
        }

        /**
         * Sets the {@code openAPIFormat} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code openAPIFormat} to set
         * @return a reference to this Builder
         */
        public Builder setOpenAPIFormat(OpenAPIFormat value) {
            openAPIFormat = value;
            return this;
        }

        /**
         * Sets the {@code excludeSwaggerFilter} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code excludeSwaggerFilter} to set
         * @return a reference to this Builder
         */
        public Builder setExcludeSwaggerFilter(String[] value) {
            excludeSwaggerFilter = value;
            return this;
        }

        /**
         * Sets the {@code removeSecurity} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param value the {@code removeSecurity} to set
         * @return a reference to this Builder
         */
        public Builder setRemoveSecurity(boolean value) {
            removeSecurity = value;
            return this;
        }

        /**
         * Returns a {@code ConvertArguments} built from the parameters previously set.
         *
         * @return a {@code ConvertArguments} built with parameters of this {@code ConvertArguments.Builder}
         */
        public ExecuteConvertion build() {
            return new ExecuteConvertion(this);
        }
    }
}
