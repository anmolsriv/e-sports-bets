package net.esportsbets;

import static org.assertj.core.api.Assertions.assertThat;

import net.esportsbets.dao.User;
import net.esportsbets.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.sql.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository repo;
	
	@Test
	public void testCreateUser() {

		User prevSavedUser = repo.findByEmail("name1@email.com");
		if ( prevSavedUser != null ) {
			repo.deleteById( prevSavedUser.getId() );
		}

		User user = new User();
		user.setEmail("name1@email.com");
		user.setPassword("email123");
		user.setFirstName("name");
		user.setLastName("name1");
		user.setDob(new Date( new java.util.Date().getTime()));
		
		User savedUser = repo.save(user);
		
		User existUser = entityManager.find(User.class, savedUser.getId());

		assertThat(user.getEmail()).isEqualTo(existUser.getEmail());

		repo.delete( savedUser );
		
	}
}
