package dev.shashank.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	private static List<User> users = new ArrayList<>();
	private static int usersCount = 0;

//	The code is enclosed within a static block. 
//	This means it executes only once, immediately when 
//	the class is loaded into memory. It's often used for 
//	initialization tasks that need to happen before the class 
//	is used.
	static {
		users.add(new User(++usersCount, "Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++usersCount, "Eve", LocalDate.now().minusYears(25)));
		users.add(new User(++usersCount, "Jim", LocalDate.now().minusYears(20)));
	}

	public List<User> findAll() {
		return users;
	}

	public User findOne(int id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
//		return users.stream().filter(predicate).findFirst().get();
		return users.stream().filter(predicate).findFirst().orElse(null);
	}
//	Predicate<? super User> predicate = user -> user.getId().equals(id);
//	Predicate<? super User> predicate, this part creates a Predicate object, 
//	a functional interface used for filtering elements.
//	It is assigned a lambda function that checks if a given User object has an ID 
//	that matches the provided id.

//	? super User: This part defines the type of argument the lambda 
//	function can accept.
//
//	The ? signifies a wildcard, indicating some type.
//	super User specifies that the type can be either User itself or 
//	any superclass of User.

	public User save(User user) {
		user.setId(++usersCount);
		users.add(user);
		return user;
	}

	public void deleteById(int id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		users.removeIf(predicate);
	}
}
