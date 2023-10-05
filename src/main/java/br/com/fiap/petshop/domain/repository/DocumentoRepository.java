package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.Documento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DocumentoRepository implements Repository<Documento, Long> {

    private static final AtomicReference<DocumentoRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private DocumentoRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static DocumentoRepository build(EntityManager manager) {
        DocumentoRepository result = instance.get();
        if (Objects.isNull( result )) {
            DocumentoRepository repo = new DocumentoRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Documento> findAll() {
        String jpql = "FROM Documento d";
        List<Documento> list = manager.createQuery(jpql).getResultList();
        return list;
    }

    @Override
    public Documento findById(Long id) {
        Documento documento = manager.find(Documento.class, id);
        return documento;
    }

    @Override
    public List<Documento> findByTexto(String texto) {
        String jpql = "FROM Documento d where Lower(d.numero)=:numero";
        Query query = manager.createQuery(jpql);
        query.setParameter("numero", texto);
        List<Documento> list = query.getResultList();
        return list;
    }

    @Override
    public Documento persist(Documento documento) {
        manager.getTransaction().begin();
        manager.persist(documento);
        manager.getTransaction().commit();
        return documento;
    }

    @Override
    public Documento update(Documento documento) {
        manager.getTransaction().begin();
        Documento documento_atualizar = manager.merge(documento);
        manager.getTransaction().commit();
        return documento_atualizar;
    }

    @Override
    public boolean delete(Documento documento) {
        manager.getTransaction().begin();
        Documento documento_deletar = manager.find(Documento.class, documento.getId());
        if (documento_deletar != null) {
            manager.remove(documento_deletar);
            manager.getTransaction().commit();
            return true;
        }
        manager.getTransaction().rollback();
        return false;
    }
}