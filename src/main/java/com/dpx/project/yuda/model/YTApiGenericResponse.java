package com.dpx.project.yuda.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YTApiGenericResponse {

    private String kind;

    private String etag;

    private PageInfo pageInfo;

    List<JsonNode> items;
}
