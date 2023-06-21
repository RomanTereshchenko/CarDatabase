package com.foxminded.javaspring.cardb.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser
class WithMockUserTests {

	@PreAuthorize("authenticated")
	public String getMessage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "Hello " + authentication;
	}

	@Test
	void getMessageUnauthenticated() {
		assertDoesNotThrow(() -> getMessage());
	}

	@Test
	@WithMockUser
	void getMessageWithMockUser() {
		String message = getMessage();
		assertThat(message.contains("Username=user"));
	}

	@Test
	@WithMockUser("customUsername")
	void getMessageWithMockUserCustomUsername() {
		String message = getMessage();
		assertThat(message.contains("Username=customUsername"));
	}

	@Test
	@WithMockUser(username = "manager", roles = { "MANAGER" })
	void getMessageWithMockUserCustomUser() {
		String message = getMessage();
		assertThat(message.contains("Granted Authorities=[ROLE_MANAGER]"));
	}

}
