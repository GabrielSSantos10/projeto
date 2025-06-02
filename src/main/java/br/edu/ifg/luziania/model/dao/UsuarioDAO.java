package br.edu.ifg.luziania.model.dao;

import br.edu.ifg.luziania.model.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@ApplicationScoped
public class UsuarioDAO implements PanacheRepository<Usuario> {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Usuario> listAll() {
        return getEntityManager().createQuery("FROM Usuario", Usuario.class).getResultList();
    }


    public Usuario buscarPorUsername(String username) {
        try {
            return entityManager.createQuery("SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> listarTodos() {
        String jpql = "SELECT u FROM Usuario u";
        return entityManager.createQuery(jpql, Usuario.class).getResultList();
    }

    public List<Usuario> pesquisarUsuarios(String nome, String email, String username) {
        StringBuilder queryStr = new StringBuilder("SELECT u FROM Usuario u WHERE 1=1");

        if (nome != null && !nome.isEmpty()) {
            queryStr.append(" AND u.nome LIKE :nome");
        }
        if (email != null && !email.isEmpty()) {
            queryStr.append(" AND u.email LIKE :email");
        }
        if (username != null && !username.isEmpty()) {
            queryStr.append(" AND u.username LIKE :username");
        }

        TypedQuery<Usuario> query = entityManager.createQuery(queryStr.toString(), Usuario.class);

        if (nome != null && !nome.isEmpty()) {
            query.setParameter("nome", "%" + nome + "%");
        }
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", "%" + email + "%");
        }
        if (username != null && !username.isEmpty()) {
            query.setParameter("username", "%" + username + "%");
        }

        return query.getResultList();
    }

    public Usuario buscarPorCpf(String cpf) {
        try {
            return entityManager
                    .createQuery("SELECT u FROM Usuario u WHERE u.cpf = :cpf", Usuario.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void persist(Usuario usuario) {
        entityManager.merge(usuario);
    }
}
