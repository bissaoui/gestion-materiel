package com.gestion.materiel.Dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class MarcheDto {
    private Long id;
    private String name;
    private LocalDate date;
    private LocalDate dateOrdreService;
    private Integer delaiExecution;
    private LocalDate dateReceptionProvisoire;
    private LocalDate dateReceptionDefinitive;
    private String typeMarche;
    private Long prestataireId;
    private Boolean hasRetenueGarantie;
    private Set<Long> materielIds;
}





