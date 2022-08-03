package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.ArtifactSimpleTypeName;
import org.metadatacenter.cedar.webapi.ValidateArtifactRequest;
import org.metadatacenter.cedar.webapi.ValidateArtifactResponse;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@Component
@Command(name = "validate-artifact")
public class ValidateArtifactCommand implements CedarCliCommand {

    @CommandLine.Mixin
    CedarApiKeyMixin apiKey;

    @Option(names = "--in",
            required = true,
            description = "The path to a file containing the JSON serialization of the artifact.")
    private Path artifact;

    @Option(names = "--artifact-type",
            required = true,
            description = "The type of artifact to validate.  One of ${COMPLETION-CANDIDATES}.")
    private ArtifactSimpleTypeName artifactType;

    private final ValidateArtifactRequest request;

    public ValidateArtifactCommand(ValidateArtifactRequest request) {
        this.request = request;
    }

    @Override
    public Integer call() throws Exception {
        var serialization = Files.readString(artifact, StandardCharsets.UTF_8);
        var result = request.send(serialization, artifactType, apiKey.getApiKey());
        System.err.println("Valid: " + result.validates());
        result.errors().stream().map(ValidateArtifactResponse.ValidationError::message).forEach(m -> System.err.printf("Error: %s\n", m));
        return 0;
    }
}