package com.dpx.project.yuda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model Class for Page Info.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageInfo {

    private int totalResults;

    private int resultsPerPage;
}
