package me.seung.demorestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.Table;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Rule
    public ExpectedException expectedException =ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUserName(){
        String password = "keesun";
        String username = "keesun@gmail.com";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountRepository.save(account);
        // When
        UserDetailsService userDetailsService= (UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);

    }

    @Test
    public void findByUsernameFail(){
        String username = "random@email.com";

        // 1. assertThrows로 예외 타입만 확인하기
        //assertThrows(UsernameNotFoundException.class, ()->accountService.loadUserByUsername(username));

        // 2. try catch로 에러 메세지 확인하기
        // 장황해지긴 하지만 에러 메세지까지 알 수 있음
        try {
            accountService.loadUserByUsername(username);
            fail("supposed to be failed");
        }catch (UsernameNotFoundException e){
            assertThat(e.getMessage()).containsSequence(username);
        }

        // 3. ExpectedException로 에러를 미리 예측하기
        // junit 4에 해당하는 코드, 5는 에러남
        //Expected

        // 에러를 예측하기..
        /*expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        accountService.loadUserByUsername(username);*/

    }
}