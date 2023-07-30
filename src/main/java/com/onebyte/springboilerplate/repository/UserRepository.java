package com.onebyte.springboilerplate.repository;

import static com.onebyte.springboilerplate.entity.QUser.user;

import com.onebyte.springboilerplate.entity.QUser;
import com.onebyte.springboilerplate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserRepository {

  @PersistenceContext
  private EntityManager em;

  public User findUser(int id) {
    String jpql = "select u from User u where u.id = :id";
    User result = em.createQuery(jpql, User.class)
        .setParameter("id", id)
        .getSingleResult();
    return result;
  }

  public User save(User user) {
    em.persist(user);
    return user;
  }
}
