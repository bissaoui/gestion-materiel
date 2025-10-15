package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.PrestataireDto;
import com.gestion.materiel.model.Prestataire;
import com.gestion.materiel.repository.PrestataireRepository;
import com.gestion.materiel.service.PrestataireService;
import com.gestion.materiel.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestataireServiceImpl implements PrestataireService {

    @Autowired
    private PrestataireRepository prestataireRepository;

    @Override
    public PrestataireDto save(PrestataireDto dto) {
        Prestataire entity = new Prestataire();
        entity.setRaisonSocial(dto.getRaisonSocial());
        entity.setNumeroTele(dto.getNumeroTele());
        entity = prestataireRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public PrestataireDto update(Long id, PrestataireDto dto) {
        Prestataire entity = prestataireRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prestataire", id));
        entity.setRaisonSocial(dto.getRaisonSocial());
        entity.setNumeroTele(dto.getNumeroTele());
        entity = prestataireRepository.save(entity);
        return toDto(entity);
    }

    @Override
    public List<PrestataireDto> findAll() {
        return prestataireRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PrestataireDto findById(Long id) {
        Prestataire entity = prestataireRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prestataire", id));
        return toDto(entity);
    }

    @Override
    public void delete(Long id) {
        prestataireRepository.deleteById(id);
    }

    private PrestataireDto toDto(Prestataire entity) {
        PrestataireDto dto = new PrestataireDto();
        dto.setId(entity.getId());
        dto.setRaisonSocial(entity.getRaisonSocial());
        dto.setNumeroTele(entity.getNumeroTele());
        return dto;
    }
}
