package br.com.fiap.petshop.domain.repository;

import br.com.fiap.petshop.domain.entity.animal.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AnimalRepository implements Repository<Animal, Long> {

    private static final AtomicReference<AnimalRepository> instance = new AtomicReference<>();
    private EntityManager manager;

    private AnimalRepository(EntityManager manager) {
        this.manager = manager;
    }

    public static AnimalRepository build(EntityManager manager) {
        AnimalRepository result = instance.get();
        if (Objects.isNull( result )) {
            AnimalRepository repo = new AnimalRepository( manager );
            if (instance.compareAndSet( null, repo )) {
                result = repo;
            } else {
                result = instance.get();
            }
        }
        return result;
    }


    @Override
    public List<Animal> findAll() {
        String jpql = "FROM Animal a";
        List<Animal> list = manager.createQuery(jpql).getResultList();
        return list;
    }

    @Override
    public Animal findById(Long id) {
        Animal animal = manager.find(Animal.class, id);
        return animal;
    }

    @Override
    public List<Animal> findByTexto(String texto) {
        String jpql = "FROM Animal a where Lower(a.nome)=:nome";
        Query query = manager.createQuery(jpql);
        query.setParameter("nome", texto.toLowerCase());
        List<Animal> list = query.getResultList();
        return list;
    }

    @Override
    public Animal persist(Animal animal) {
        manager.getTransaction().begin();
        manager.persist(animal);
        manager.getTransaction().commit();
        return animal;
    }

    @Override
    public Animal update(Animal animal) {
        manager.getTransaction().begin();
        Animal animal_atualizar = manager.merge(animal);
        manager.getTransaction().commit();
        return animal_atualizar;
    }

    @Override
    public boolean delete(Animal animal) {
        manager.getTransaction().begin();
        Animal animal_deletar = manager.find(Animal.class, animal.getId());
        if (animal_deletar != null) {
            manager.remove(animal_deletar);
            manager.getTransaction().commit();
            return true;
        }
        manager.getTransaction().rollback();
        return false;
    }
}