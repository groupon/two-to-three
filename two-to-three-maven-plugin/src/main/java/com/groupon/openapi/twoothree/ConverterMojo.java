package com.groupon.openapi.twoothree;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "convert", threadSafe = true)
public class ConverterMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
    private String projectBuildDir;

    /**
     * The base swagger file to convert.
     */
    @Parameter(name = "input", required = true)
    private File input;

    /**
     * file to output the converted swagger
     */
    @Parameter(name = "output")
    private String output;

    /**
     * file to output the converted swagger
     */
    @Parameter(name = "additionalSwaggerPath")
    private String additionalSwaggerPath;

    /**
     * suffix to filter the swaggers to merge
     */
    @Parameter(name = "filterSwaggerSuffix")
    private String filterSwaggerSuffix;

    /**
     * tags to be excluded from the conversion, comma separated
     */
    @Parameter(name = "tagsToExclude")
    private String[] tagsToExclude = new String[]{};

    /**
     * output as openapi3 (default) or swagger/openapi2
     */
    @Parameter(name = "format")
    private OpenAPIFormat openAPIFormat = OpenAPIFormat.openapi3;

    /**
     * swaggers to exclude
     */
    @Parameter(name = "excludeSwaggerFilter")
    private String[] excludeSwaggerFilter = new String[]{};

    /**
     * remove Security
     */
    @Parameter(name = "removeSecurity")
    private boolean removeSecurity = false;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String fileOutput = this.output;
        if (output == null || output.trim().isEmpty()) {
            fileOutput = projectBuildDir;
            if (OpenAPIFormat.openapi3.equals(openAPIFormat)) {
                fileOutput = fileOutput + "/openapi.yaml";
            } else {
                fileOutput = fileOutput + "/swagger.yaml";
            }
        }
        try {
            new ExecuteConvertion.Builder().setFile(input)
                    .setOutput(fileOutput)
                    .setAdditionalSwaggerPath(additionalSwaggerPath)
                    .setExcludeSwaggerFilter(excludeSwaggerFilter)
                    .setFilterSwaggerSuffix(filterSwaggerSuffix)
                    .setOpenAPIFormat(openAPIFormat)
                    .setRemoveSecurity(removeSecurity)
                    .setTagsToExclude(tagsToExclude)
            .build().call();
        } catch (Exception e) {
            throw new MojoFailureException("count not execute", e);
        }
    }
}
