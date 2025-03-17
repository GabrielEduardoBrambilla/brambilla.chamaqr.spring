package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Repository.ChamadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadaService {
    @Autowired
    private ChamadaRepository chamadaRepository;

    public List<Chamada> getAllChamadas() {
        return chamadaRepository.findAll();
    }
    public List<Chamada> getChamadasByQtdQrs(Long qtdQrs) {
        return chamadaRepository.findByQtdQrs(qtdQrs);
    }

    public List<Chamada> getChamadasFromLastMonth() {
        LocalDateTime startOfThisMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime startOfLastMonth = startOfThisMonth.minusMonths(1);

        return chamadaRepository.findChamadasFromLastMonth(startOfLastMonth, startOfThisMonth);
    }

    public Optional<Chamada> getChamadaById(Long id) {
        return chamadaRepository.findById(id);
    }

    public Chamada saveChamada(Chamada chamada) {
        return chamadaRepository.save(chamada);
    }

    public void deleteChamada(Long id) {
        chamadaRepository.deleteById(id);
    }
}