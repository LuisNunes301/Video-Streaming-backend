package com.mininetflix.ministreaming.infrastructure.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.profilefavorite.port.ProfileFavoriteRepository;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.AddFavoriteUseCase;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.AddFavoriteUseCaseImpl;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.ListFavoritesUseCase;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.ListFavoritesUseCaseImpl;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.RemoveFavoriteUseCase;
import com.mininetflix.ministreaming.application.profilefavorite.usecase.RemoveFavoriteUseCaseImpl;
import com.mininetflix.ministreaming.application.user.port.PasswordEncoder;
import com.mininetflix.ministreaming.application.user.port.TokenService;
import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.application.user.usecase.AuthenticateUserUseCase;
import com.mininetflix.ministreaming.application.user.usecase.AuthenticateUserUseCaseImpl;
import com.mininetflix.ministreaming.application.user.usecase.RegisterUserUseCase;
import com.mininetflix.ministreaming.application.user.usecase.RegisterUserUseCaseImpl;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;

@Configuration
public class UserUseCaseConfig {

        @Bean
        public AuthenticateUserUseCase authenticateUserUseCase(
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        TokenService tokenService) {

                return new AuthenticateUserUseCaseImpl(
                                userRepository,
                                passwordEncoder,
                                tokenService);
        }

        @Bean
        public RegisterUserUseCase registerUserUseCase(
                        UserRepository userRepository,
                        UserProfileRepository userProfileRepository,
                        PasswordEncoder passwordEncoder) {

                return new RegisterUserUseCaseImpl(
                                userRepository,
                                userProfileRepository,
                                passwordEncoder);
        }

        @Bean
        public AddFavoriteUseCase addFavoriteUseCase(
                        ProfileFavoriteRepository repository,
                        UserProfileRepository profileRepository) {

                return new AddFavoriteUseCaseImpl(
                                repository,
                                profileRepository);
        }

        @Bean
        public RemoveFavoriteUseCase removeFavoriteUseCase(
                        ProfileFavoriteRepository repository,
                        UserProfileRepository profileRepository) {

                return new RemoveFavoriteUseCaseImpl(
                                repository,
                                profileRepository);
        }

        @Bean
        public ListFavoritesUseCase listFavoritesUseCase(
                        ProfileFavoriteRepository repository,
                        UserProfileRepository profileRepository,
                        VideoCatalogRepository videoCatalogRepository) {

                return new ListFavoritesUseCaseImpl(
                                repository,
                                profileRepository,
                                videoCatalogRepository);
        }
}