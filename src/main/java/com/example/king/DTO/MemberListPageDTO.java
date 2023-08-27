package com.example.king.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class MemberListPageDTO {
    List<MemberListDTO> memberListDTOList;

    private int currentPage;
    private int pageSize;
    private long totalElements;

    @Override
    public String toString() {
        return "MemberListPageDTO{" +
                "memberListDTOList=" + memberListDTOList +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalElements=" + totalElements +
                '}';
    }
}
