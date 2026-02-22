package com.example.cryptoconnect.repository;
	
import java.util.List;
import java.util.Optional;
	
import org.springframework.data.jpa.repository.JpaRepository;
	
import com.example.cryptoconnect.entity.User;
	
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	boolean existsByUsername(String username); 
	List<User> findByUsernameNot(String username);
	
}