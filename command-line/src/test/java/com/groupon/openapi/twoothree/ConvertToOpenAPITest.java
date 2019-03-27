package com.groupon.openapi.twoothree;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.Test;

public class ConvertToOpenAPITest {

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void convert() throws IOException {
        ConvertToOpenAPI converter = new ConvertToOpenAPI();
        Swagger20Parser parser = new Swagger20Parser();
        Swagger swagger = parser.read("/swagger2.yml", Collections.emptyList());
        OpenAPI openAPI = converter.convert(swagger);
        Paths paths = openAPI.getPaths();
        Set<Map.Entry<String, PathItem>> pathSet = paths.entrySet();
        assertThat(pathSet).isNotNull();
        Map.Entry<String, PathItem> pathEntry = pathSet.iterator().next();
        assertThat(pathEntry.getValue().getGet()).isNotNull();
        List<Parameter> parameters = pathEntry.getValue().getGet().getParameters();
        Parameter first = parameters.get(0);
        assertThat(first.get$ref()).isNotEmpty();
        Parameter second = parameters.get(1);
        assertThat(second.getSchema().getType()).isEqualTo("string");
        assertThat(second.getSchema().getFormat()).isNull();
        Parameter third = parameters.get(2);
        assertThat(third.getSchema().getType()).isEqualTo("integer");
        assertThat(third.getSchema().getFormat()).isEqualTo("int32");
        Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
        assertThat(schemas).isNotNull();
        List<Schema> list = new ArrayList<>(schemas.values());
        Schema firstSchema = list.get(0);
        assertThat(firstSchema.getProperties()).isNotNull();
        Schema orderId = (Schema) firstSchema.getProperties().get("orderId");
        assertThat(orderId.getMaximum().toString()).isEqualTo("600");
        assertThat(orderId.getMinimum().toString()).isEqualTo("200");
    }
}
