package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Telefone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TelefoneRepository implements Repository<Telefone, Long> {

    private static final AtomicReference<TelefoneRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private TelefoneRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static TelefoneRepository build(EntityManager manager) {
        TelefoneRepository result = instance.get();
        if (Objects.isNull( result )) {
            TelefoneRepository repo = new TelefoneRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Telefone> findAll() {
        String jpql = "FROM Telefone t";
        List<Telefone> list = manager.createQuery(jpql).getResultList();
        return list;
    }

    @Override
    public Telefone findById(Long id) {
        Telefone telefone = manager.find(Telefone.class, id);
        return telefone;
    }

    @Override
    public List<Telefone> findByTexto(String texto) {
        String jpql = "FROM Telefone t where Lower(t.numero)=:numero";
        Query query = manager.createQuery(jpql);
        query.setParameter("numero", texto);
        List<Telefone> list = query.getResultList();
        return list;
    }

    @Override
    public Telefone persist(Telefone telefone) {
        manager.getTransaction().begin();
        manager.persist(telefone);
        manager.getTransaction().commit();
        return telefone;
    }

    @Override
    public Telefone update(Telefone telefone) {
        manager.getTransaction().begin();
        Telefone telefone_atualizar = manager.merge(telefone);
        manager.getTransaction().commit();
        return telefone_atualizar;
    }

    @Override
    public boolean delete(Telefone telefone) {
        manager.getTransaction().begin();
        Telefone telefone_deletar = manager.find(Telefone.class, telefone.getId());
        if (telefone_deletar != null) {
            manager.remove(telefone_deletar);
            manager.getTransaction().commit();
            return true;
        }
        manager.getTransaction().rollback();
        return false;
    }
}
