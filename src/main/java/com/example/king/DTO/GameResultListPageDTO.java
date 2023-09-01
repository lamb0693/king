package com.example.king.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameResultListPageDTO {

    List<GameResultListDTO> gameResultListDTOList;

    private int currentPage;
    private int pageSize;
    private long totalElements;

}
