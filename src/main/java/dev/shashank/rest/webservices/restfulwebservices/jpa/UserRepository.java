package dev.shashank.rest.webservices.restfulwebservices.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.shashank.rest.webservices.restfulwebservices.user.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
