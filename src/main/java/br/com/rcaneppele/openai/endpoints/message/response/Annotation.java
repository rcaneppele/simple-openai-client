package br.com.rcaneppele.openai.endpoints.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Annotation(
        String type,
        String text,
        @JsonProperty("file_citation")
        FileCitation fileCitation,
        @JsonProperty("file_path")
        FilePath filePath,
        @JsonProperty("start_index")
        Integer startIndex,
        @JsonProperty("end_index")
        Integer endIndex
) {}
