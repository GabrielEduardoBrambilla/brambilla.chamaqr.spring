package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Entity.Presenca;
import com.brambilla.chamadaqr.Repository.AlunoRepository;
import com.brambilla.chamadaqr.Repository.ChamadaRepository;
import com.brambilla.chamadaqr.Repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PresencaService {
    @Autowired
    private PresencaRepository presencaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ChamadaRepository chamadaRepository;

    public List<Presenca> getAllPresencas() {
        return presencaRepository.findAll();
    }

    public Optional<Presenca> getPresencaById(Long id) {
        return presencaRepository.findById(id);
    }

    public List<Presenca> getPresencasByAlunoRa(Long ra) {
        return presencaRepository.findByAlunoRa(ra);
    }

    public List<Presenca> getPresencasByChamadaId(Long chamadaId) {
        return presencaRepository.findByChamadaId(chamadaId);
    }

    public Presenca savePresenca(Presenca presenca) {
        Optional<Aluno> aluno = alunoRepository.findById(presenca.getAluno().getId());
        Optional<Chamada> chamada = chamadaRepository.findById(presenca.getChamada().getId());

        if (aluno.isPresent() && chamada.isPresent()) {
            return presencaRepository.save(presenca);
        } else {
            throw new RuntimeException("Aluno or Chamada not found. Please provide valid IDs.");
        }
    }

    public void deletePresenca(Long id) {
        presencaRepository.deleteById(id);
    }
}
