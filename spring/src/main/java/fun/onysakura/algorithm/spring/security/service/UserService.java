package fun.onysakura.algorithm.spring.security.service;

import fun.onysakura.algorithm.spring.security.model.UserModel;
import fun.onysakura.algorithm.spring.security.model.UserRoleModel;
import fun.onysakura.algorithm.spring.security.repository.UserRepository;
import fun.onysakura.algorithm.spring.security.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> optionalUserModel = userRepository.findFirstByUsername(username);
        UserModel userModel = optionalUserModel.orElseThrow(() -> new UsernameNotFoundException("用户 " + username + " 不存在！"));
        List<UserRoleModel> userRoleModels = userRoleRepository.findAllByUsername(username);
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UserRoleModel userRoleModel : userRoleModels) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRoleModel.getRole());
            authorities.add(grantedAuthority);
        }
        userModel.setAuthorities(authorities);
        return userModel;
    }

    public List<GrantedAuthority> getAuthorities(String username) {
        return userRoleRepository.findAllByUsername(username).stream()
                .map(UserRoleModel::getRole)
                .collect(ArrayList::new, (list, role) -> list.add(new SimpleGrantedAuthority(role)), ArrayList::addAll);
    }


    public UserModel getUserModel(String username) throws UsernameNotFoundException {
        Optional<UserModel> optionalUserModel = userRepository.findFirstByUsername(username);
        return optionalUserModel.orElseThrow(() -> new UsernameNotFoundException("用户 " + username + " 不存在！"));
    }

    public UserModel save(UserModel userModel) {
        return userRepository.saveAndFlush(userModel);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserModel register(UserModel userModel) {
        final String username = userModel.getUsername();
        try {
            return getUserModel(username);
        } catch (UsernameNotFoundException ignored) {
            userModel.setLastPasswordResetDate(new Date());
            userModel = save(userModel);
            updateUserRole(userModel);
            return userModel;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(UserModel userModel) {
        userRoleRepository.deleteAllByUsername(userModel.getUsername());
        for (GrantedAuthority authority : userModel.getAuthorities()) {
            UserRoleModel userRoleModel = new UserRoleModel();
            userRoleModel.setUsername(userModel.getUsername());
            userRoleModel.setRole(authority.getAuthority());
            userRoleRepository.save(userRoleModel);
        }
        userRoleRepository.flush();
    }
}
