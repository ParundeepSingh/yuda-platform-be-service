package com.dpx.project.yuda.model.trends;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewCountTrendResponse {

    private String videoId;

    private List<ViewCountRecord> viewCount;
}
