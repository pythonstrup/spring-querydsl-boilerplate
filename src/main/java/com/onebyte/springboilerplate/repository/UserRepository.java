package com.onebyte.springboilerplate.repository;

import com.onebyte.springboilerplate.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

  @PersistenceContext
  private EntityManager em;

  public int save(UserEntity userEntity) {
    em.persist(userEntity);
    return userEntity.getId();
  }
}
