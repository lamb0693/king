package com.example.king.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RankingListPageDTO {

    List<RankingDTO> rankingDTOList;

    private int currentPage;
    private int pageSize;
    private long totalElements;

}
