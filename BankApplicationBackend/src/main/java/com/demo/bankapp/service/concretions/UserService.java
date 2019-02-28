package com.demo.bankapp.service.concretions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.bankapp.exception.BadCredentialsException;
import com.demo.bankapp.exception.UserNotFoundException;
import com.demo.bankapp.model.User;
import com.demo.bankapp.repository.UserRepository;
import com.demo.bankapp.service.abstractions.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<User> findAll() {
		return repository.findAll();
	}

	@Override
	public User createNewUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}

	@Override
	public User login(String username, String password) {

		User user = findByUserName(username);
		String encodedPassword = passwordEncoder.encode(password);

		if (!encodedPassword.equals(user.getPassword())) {
			throw new BadCredentialsException();
		}

		return user;
	}

	@Override
	public User findByUserName(String username) {
		User user = repository.findByUsername(username);

		if (user == null)
			throw new UserNotFoundException(username);
		else
			return user;
	}

	@Override
	public User findByTcno(String tcno) {
		User user = repository.findByTcno(tcno);

		if (user == null)
			throw new UserNotFoundException("with TC No: " + tcno);
		else
			return user;
	}

	// Avoid timing attacks?
	// private boolean isEqual(byte[] a, byte[] b) {
	// if (a.length != b.length) {
	// return false;
	// }
	//
	// int result = 0;
	// for (int i = 0; i < a.length; i++) {
	// result |= a[i] ^ b[i];
	// }
	// return result == 0;
	// }

}
