package dev.shashank.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.shashank.rest.webservices.restfulwebservices.jpa.PostRepository;
import dev.shashank.rest.webservices.restfulwebservices.jpa.UserRepository;
import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	private UserRepository userRepository;
	private PostRepository postRepository;

	public UserJpaResource(UserRepository repository, PostRepository postRepository) {
		this.userRepository = repository;
		this.postRepository = postRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	// http://localhost:8080/users

	// EntityModel
	// WebMvcLinkBuilder

	@GetMapping("/jpa/users/{user_id}")
	public EntityModel<User> retrieveUser(@PathVariable int user_id) {
		Optional<User> user = userRepository.findById(user_id);

		if (user.isEmpty())
			throw new UserNotFoundException("user_id:" + user_id);

		EntityModel<User> entityModel = EntityModel.of(user.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));

		return entityModel;
	}

	// own function to get a particular post, not covered in course
	@GetMapping("/jpa/users/{user_id}/posts/{post_id}")
	public EntityModel<Post> retrievePost(@PathVariable int user_id, @PathVariable int post_id) {
		Optional<Post> post = postRepository.findById(post_id);
		if (post.isEmpty())
			throw new PostNotFoundException("post_id:" + post_id);
		EntityModel<Post> entityModel = EntityModel.of(post.get());
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrievePostsForUser(user_id));
		entityModel.add(link.withRel("all-posts"));
		return entityModel;
	}

	@DeleteMapping("/jpa/users/{user_id}")
	public void deleteUser(@PathVariable int user_id) {
		userRepository.deleteById(user_id);
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{user_id}")
				.buildAndExpand(savedUser.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping("/jpa/users/{user_id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int user_id) {
		Optional<User> user = userRepository.findById(user_id);

		if (user.isEmpty())
			throw new UserNotFoundException("user_id:" + user_id);

		return user.get().getPosts();

	}

	@PostMapping("/jpa/users/{user_id}/posts")
	public ResponseEntity<Object> createPostForUser(@PathVariable int user_id, @Valid @RequestBody Post post) {
		Optional<User> user = userRepository.findById(user_id);
		if (user.isEmpty())
			throw new UserNotFoundException("user_id:" + user_id);
		post.setUser(user.get());
		Post savedPost = postRepository.save(post);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{user_id}")
				.buildAndExpand(savedPost.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}
