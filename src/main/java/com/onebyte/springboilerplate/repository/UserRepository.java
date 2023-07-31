package com.onebyte.springboilerplate.repository;

import static com.onebyte.springboilerplate.entity.QUser.user;

import com.onebyte.springboilerplate.dto.UserSearchCondition;
import com.onebyte.springboilerplate.entity.QUser;
import com.onebyte.springboilerplate.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@Transactional
public class UserRepository {

  @PersistenceContext
  private EntityManager em;
  private final JPAQueryFactory queryFactory;

  public UserRepository() {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public User findUser(int id) {
    String jpql = "select u from User u where u.id = :id";
    User result = em.createQuery(jpql, User.class)
        .setParameter("id", id)
        .getSingleResult();
    return result;
  }

  public List<User> findUserAll() {
    String jpql = "select u from User u";
    List<User> resultList = em.createQuery(jpql, User.class)
        .getResultList();
    return resultList;
  }

  public List<User> searchUser(UserSearchCondition search) {
    return queryFactory
        .select(user)
        .from(user)
        .where(
            usernameEq(search.getUsername()),
            ageEq(search.getAge())
        )
        .fetch();
  }

  public User save(User user) {
    em.persist(user);
    return user;
  }

  private BooleanExpression usernameEq(String username) {
    return StringUtils.hasText(username) ? user.username.eq(username): null;
  }

  private BooleanExpression ageEq(Integer age) {
    return age != null ? user.age.eq(age): null;
  }
}
