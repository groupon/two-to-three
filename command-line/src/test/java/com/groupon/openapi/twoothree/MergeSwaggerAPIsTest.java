package com.groupon.openapi.twoothree;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.groupon.openapi.twoothree.util.IOUtil;


class MergeSwaggerAPIsTest {

    @Test
    @DisplayName("merge swagger files into a single One")
    void testMerge() throws IOException {
        String dataMain = getSwaggerData("/baseSwagger.yml");

        Swagger20Parser swagger20Parser = new Swagger20Parser();
        Swagger swagger1 = swagger20Parser.read("/swagger1.yml", Collections.emptyList());
        Swagger swagger2 = swagger20Parser.read("/swagger2.yml", Collections.emptyList());
        Swagger swagger3 = swagger20Parser.read("/swagger3.yml", Collections.emptyList());
        Swagger mainSwagger = swagger20Parser.parse(dataMain);
        Swagger mergedSwagger = new MergeSwaggerAPIs(mainSwagger, Collections.emptyList()).merge(swagger1, swagger2, swagger3);
        assertThat(mergedSwagger.getPaths()).isNotEmpty().hasSize(4);
        Map<String, Path> paths = new HashMap<>(mergedSwagger.getPaths());

        verifyPath(swagger1, paths);
        verifyPath(swagger2, paths);
        verifyPath(swagger3, paths);

        Map<String, Model> definitions = mergedSwagger.getDefinitions();
        verifyDefinitions(swagger1, definitions);
        verifyDefinitions(swagger2, definitions);
        verifyDefinitions(swagger3, definitions);
        assertThat(mergedSwagger.getInfo().getTitle()).isEqualTo("Joined Spec Title");
        assertThat(mergedSwagger.getInfo().getDescription()).isEqualTo("Joined Spec description");
        assertThat(mergedSwagger.getTags()).hasSize(2);
        assertThat(mergedSwagger.getTags().get(0).getName()).isEqualTo("TagToRemove");
        assertThat(mergedSwagger.getTags().get(1).getName()).isEqualTo("TagToKeep");
        assertThat(mergedSwagger.getParameters()).isNotEmpty().hasSize(1);
    }

    @Test
    void testMergeTagToRemove() throws IOException {
        String dataMain = getSwaggerData("/baseSwagger.yml");

        Swagger20Parser swagger20Parser = new Swagger20Parser();

        Swagger swagger3 = swagger20Parser.read("/swagger3.yml", Collections.emptyList());
        Swagger mainSwagger = swagger20Parser.parse(dataMain);
        Swagger mergedSwagger = new MergeSwaggerAPIs(mainSwagger, Collections.singletonList("TagToRemove")).merge(swagger3);
        assertThat(mergedSwagger.getPaths()).isNotEmpty().hasSize(1);

        Map<String, Model> definitions = mergedSwagger.getDefinitions();

        verifyDefinitions(swagger3, definitions);
        assertThat(mergedSwagger.getInfo().getTitle()).isEqualTo("Joined Spec Title");
        assertThat(mergedSwagger.getInfo().getDescription()).isEqualTo("Joined Spec description");
        assertThat(mergedSwagger.getTags()).hasSize(1);
        assertThat(mergedSwagger.getTags().get(0).getName()).isEqualTo("TagToKeep");
        assertThat(mergedSwagger.getPaths()).hasSize(1).containsKeys("/v2/{countryCode}/endpointThree");
    }

    private void verifyDefinitions(Swagger swagger1, Map<String, Model> definitions) {
        Map<String, Model> definitions1 = swagger1.getDefinitions();
        assertThat(definitions).containsKeys(definitions1.keySet().toArray(new String[]{}));
        assertThat(definitions).containsValues(definitions1.values().toArray(new Model[]{}));
    }

    private void verifyPath(Swagger swagger1, Map<String, Path> paths) {
        Map<String, Path> pathMap = swagger1.getPaths();
        pathMap.forEach((key, path) -> {
            Path removed = paths.remove(key);
            assertThat(removed).isEqualToComparingFieldByField(path);
        });
    }

    private String getSwaggerData(String name) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(name);
        return new String(IOUtil.toByteArray(inputStream));
    }
}
