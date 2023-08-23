package com.onebyte.springboilerplate.domain.repository;

import static com.onebyte.springboilerplate.domain.entity.QUser.user;

import com.onebyte.springboilerplate.domain.entity.User;
import com.onebyte.springboilerplate.domain.dto.user.UserSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@Transactional
public class UserRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public UserRepository(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  public User findUser(int id) {
    String jpql = "select u from User u where u.id = :id";
    User result = em.createQuery(jpql, User.class)
        .setParameter("id", id)
        .getSingleResult();
    return result;
  }

  public Optional<User> findUserByUsername(String username) {
    String jpql = "select u from User u where u.username = :username";
    List<User> userList = em.createQuery(jpql, User.class)
        .setParameter("username", username)
        .getResultList();
    return userList.stream().findAny();
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
