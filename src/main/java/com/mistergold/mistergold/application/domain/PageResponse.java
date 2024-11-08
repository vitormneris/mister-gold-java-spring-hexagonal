package com.mistergold.mistergold.application.domain;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    Integer pageSize;
    Long totalElements;
    Integer totalPages;
    Integer currentPage;
    Integer nextPage;
    Integer previousPage;
    List<T> content;
}