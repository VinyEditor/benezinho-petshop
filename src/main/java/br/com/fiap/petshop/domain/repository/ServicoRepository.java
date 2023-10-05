package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.servico.Servico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ServicoRepository implements Repository<Servico, Long>{

    private static final AtomicReference<ServicoRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private ServicoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static ServicoRepository build(EntityManager manager) {
        ServicoRepository result = instance.get();
        if (Objects.isNull( result )) {
            ServicoRepository repo = new ServicoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Servico> findAll() {
        String jpql = "FROM Servico s";
        List<Servico> list = manager.createQuery(jpql).getResultList();
        return list;
    }

    @Override
    public Servico findById(Long id) {
        Servico servico = manager.find(Servico.class, id);
        return servico;
    }

    @Override
    public List<Servico> findByTexto(String texto) {
        String jpql = "FROM Servico s where Lower(s.tp_servico)=:tp_servico";
        Query query = manager.createQuery(jpql);
        query.setParameter("tp_servico", texto.toLowerCase());
        List<Servico> list = query.getResultList();
        return list;
    }

    @Override
    public Servico persist(Servico servico) {
        manager.getTransaction().begin();
        manager.persist(servico);
        manager.getTransaction().commit();
        return servico;
    }

    @Override
    public Servico update(Servico servico) {
        manager.getTransaction().begin();
        Servico servico_atualizar = manager.merge(servico);
        manager.getTransaction().commit();
        return servico_atualizar;
    }

    @Override
    public boolean delete(Servico servico) {
        manager.getTransaction().begin();
        Servico servico_deletar = manager.find(Servico.class, servico.getId());
        if (servico_deletar != null) {
            manager.remove(servico_deletar);
            manager.getTransaction().commit();
            return true;
        }
        manager.getTransaction().rollback();
        return false;
    }
}
