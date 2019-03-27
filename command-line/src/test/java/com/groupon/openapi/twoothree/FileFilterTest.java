package com.groupon.openapi.twoothree;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FileFilterTest {

    @Test
    public void filter() throws IOException {
        final Object basedir = System.getProperties().get("basedir");
        if (basedir == null) {
            throw new IllegalArgumentException("-Dbasedir=<path to config project>");
        }
        String path = basedir.toString() + "/src/test/resources";
        List<String> files = new FileFilter(path).filter(Collections.singletonList("shouldExclude"), "/shouldInclude/src/main/resources/swagger.yml");
        assertThat(files).hasSize(1).contains(path + "/shouldInclude/src/main/resources/swagger.yml")
                .as("it should not contain shouldNotFind/swagger.yml nor  shouldExclude/src/main/resources/swagger.yml");
    }
}
