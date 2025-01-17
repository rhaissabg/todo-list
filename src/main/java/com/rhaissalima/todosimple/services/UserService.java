package com.rhaissalima.todosimple.services;

import com.rhaissalima.todosimple.models.User;
import com.rhaissalima.todosimple.repositories.UserRepository;
import com.rhaissalima.todosimple.services.exceptions.DataBindingViolationException;
import com.rhaissalima.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("User not found!"));
    }

    @Transactional // it's used when we want to persist something in db
    public User create(User obj) {
        obj.setId(null);
        obj = userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword()); // i only let the user change the password
        return userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Impossible to delete this user!");
        }
    }

}
