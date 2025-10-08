package com.gestion.materiel.Dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class MarcherDto {
    private Long id;
    private String name;
    private LocalDate date;
    private Set<Long> materielIds;
}



