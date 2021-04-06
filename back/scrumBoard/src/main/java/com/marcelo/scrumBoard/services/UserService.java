package com.marcelo.scrumBoard.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public User save(User user) {
		return userRepository.save(user);
	}
	public String hashedPassword(String password) {
		String  hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return hashed;
	}
	
	 public boolean authenticateUser(String email, String password) {
        // primero encontrar el usuario por su email
        User user = this.findByEmail(email);
        // si no lo podemos encontrar por su email, retornamos false
        if(user == null) {
            return false;
        } else {
            // si el password coincide devolvemos true, sino, devolvemos false
            if(BCrypt.checkpw(password, user.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
    }
	 
	public User findByEmail(String email) {
    	Optional<User> u = userRepository.findByEmail(email);
    	if(u.isPresent()) {
    		return u.get();
    	}else {
    		return null;
    	}
    }

	public User findById(Long id) {
		Optional<User> u = userRepository.findById(id);
		if(u.isPresent()) {
			return u.get();
		}else {
			return null;
		}
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}
}
