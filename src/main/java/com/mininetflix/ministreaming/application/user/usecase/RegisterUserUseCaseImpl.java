package com.mininetflix.ministreaming.application.user.usecase;

import com.mininetflix.ministreaming.application.user.dto.RegisterUserInput;
import com.mininetflix.ministreaming.application.user.port.PasswordEncoder;
import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.user.User;
import com.mininetflix.ministreaming.domain.user.exception.EmailAlreadyExistsException;
import com.mininetflix.ministreaming.domain.user.exception.NameAlreadyExistsExecption;
import com.mininetflix.ministreaming.domain.userprofile.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

  private final UserRepository userRepository;
  private final UserProfileRepository userProfileRepository;
  private final PasswordEncoder passwordEncoder;

  public RegisterUserUseCaseImpl(
      UserRepository userRepository,
      UserProfileRepository userProfileRepository,
      PasswordEncoder passwordEncoder) {

    this.userRepository = userRepository;
    this.userProfileRepository = userProfileRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void execute(RegisterUserInput input) {

    if (userRepository.existsByEmail(input.email())) {
      throw new EmailAlreadyExistsException(input.email());
    }

    if (userRepository.existsByName(input.name())) {
      throw new NameAlreadyExistsExecption(input.name());
    }

    User user = new User(input.name(), input.email(), passwordEncoder.encode(input.password()));

    User savedUser = userRepository.save(user);
    UserProfile profile = UserProfile.create(savedUser.getId(), savedUser.getName());

    userProfileRepository.save(profile);
  }
}
