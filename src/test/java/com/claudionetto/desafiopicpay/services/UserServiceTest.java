package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.converter.UserConverter;
import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.domain.user.UserType;
import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import com.claudionetto.desafiopicpay.dto.UserResponseDTO;
import com.claudionetto.desafiopicpay.exceptions.InsufficientBalanceException;
import com.claudionetto.desafiopicpay.exceptions.MerchantCannotMakeTransactionsException;
import com.claudionetto.desafiopicpay.exceptions.UserNotFoundException;
import com.claudionetto.desafiopicpay.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;

    User user;
    User userCreated;
    UserCreateDTO userCreateDTO;
    UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp(){
        this.user = new User(1L, "Claudio", "Netto", "claudio@gmail.com",
                "123123123", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        this.userCreated = new User(1L, "Claudio", "Netto", "claudio@gmail.com",
                "123123123", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        this.userResponseDTO = new UserResponseDTO("Claudio", "Netto", "claudio@gmail.com",
                "123123123",  UserType.COMMON, BigDecimal.valueOf(50));
        this.userCreateDTO = new UserCreateDTO("Claudio", "Netto", "claudio@gmail.com",
                "123123123", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
    }

    @Test
    @DisplayName("list all should return a list of UserResponseDTO when successful")
    void listAll_ShouldReturnListOfUserResponseDTO_WhenSuccessful(){

        List<User> userList = List.of(this.user);

        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Mockito.when(userConverter.toUserResponseDTOList(userList)).thenReturn(List.of(userResponseDTO));

        List<UserResponseDTO> userResponseDTOList = userService.listAll();

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Mockito.verify(userConverter, Mockito.times(1)).toUserResponseDTOList(userList);
        Assertions.assertThat(userResponseDTOList)
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(userResponseDTOList.get(0))
                .isEqualTo(userResponseDTO);
    }

    @Test
    @DisplayName("save should save a new user and return a userResponseDTO when successful")
    void save_ShouldSaveUserAndReturnUserResponseDTO_WhenSuccessful(){

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(this.userCreated);
        Mockito.when(userConverter.toUserResponseDTO(this.userCreated)).thenReturn(this.userResponseDTO);

        UserResponseDTO userResponse = userService.save(userCreateDTO);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(userConverter, Mockito.times(1)).toUserResponseDTO(this.userCreated);

        Assertions.assertThat(userResponse)
                .isNotNull()
                .isEqualTo(this.userResponseDTO);
    }

    @Test
    @DisplayName("updateBalance should update the user when successful")
    void updateBalance_ShouldUpdateAndReturnUser_WhenSuccessful(){

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User userResult = userService.updateBalance(this.user);

        Assertions.assertThat(userResult)
                .isNotNull()
                .isEqualTo(this.user);
    }

    @Test
    @DisplayName("findById should return a user by Long id when user is found successful")
    void findById_ShouldReturnUser_WhenUserIsFoundSuccessfully(){

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.user));
        User userResult = userService.findById(user.getId());

        Assertions.assertThat(userResult)
                .isNotNull()
                .isEqualTo(this.user);

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("findById should return a user by Long id when user is found successful")
    void findById_ShouldThrowUserNotFoundException_WhenUserIsNotFound(){

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Usuário não encontrado");

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("validateUser should return true when user is valid, user is valid when UserType isn't Merchant"
                + "and user has balance available to transaction")
    void validateUser_ShouldReturnTrue_WhenUserIsValid(){

        BigDecimal transactionValue = new BigDecimal(20);

        boolean isValid = userService.validateUser(this.user, transactionValue);

        Assertions.assertThat(isValid)
                .isTrue();
        Assertions.assertThat(this.user.getUserType())
                .isNotEqualTo(UserType.MERCHANT);
        Assertions.assertThat(this.user.getBalance())
                .isGreaterThanOrEqualTo(transactionValue);
    }

    @Test
    @DisplayName("validateUser should throw MerchantCannotMakeTransactionsException when user type is MERCHANT")
    void validateUser_ShouldThrowMerchantCannotMakeTransactionsException_WhenIsMerchantUser() {

        BigDecimal transactionValue = new BigDecimal(20);
        User merchantUser = new User(1L, "Merchant", "Shop", "merchant@example.com",
                "password", "12345678", UserType.MERCHANT, BigDecimal.valueOf(100));

        Assertions.assertThatExceptionOfType(MerchantCannotMakeTransactionsException.class)
                .isThrownBy(() -> userService.validateUser(merchantUser, transactionValue))
                .withMessage("Lojistas não podem realizar transações, somente receber");
    }

    @Test
    @DisplayName("validateUser should throw InsufficientBalanceException when user has insufficient balance")
    void validateUser_ShouldThrowInsufficientBalanceException_WhenUserHasInsufficientBalance() {

        BigDecimal transactionValue = new BigDecimal(100);

        Assertions.assertThat(this.user.getBalance())
                .isLessThan(transactionValue);
        Assertions.assertThat(this.user.getUserType())
                .isNotEqualTo(UserType.MERCHANT);

        Assertions.assertThatExceptionOfType(InsufficientBalanceException.class)
                .isThrownBy(() -> userService.validateUser(this.user, transactionValue))
                .withMessage("Saldo insuficiente para o usuário realizar a transação");
    }

}