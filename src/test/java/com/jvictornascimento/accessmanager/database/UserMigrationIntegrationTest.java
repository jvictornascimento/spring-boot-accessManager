package com.jvictornascimento.accessmanager.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class UserMigrationIntegrationTest {

	@Autowired
	private JdbcClient jdbcClient;

	@Test
	void shouldCreateUsersTable() {
		var columns = jdbcClient.sql("""
				select column_name
				from information_schema.columns
				where table_name = 'users'
				and table_schema = 'public'
				order by ordinal_position
				""")
			.query(String.class)
			.list();

		assertThat(columns)
			.containsExactly("id", "name", "email", "password_hash", "active", "created_at", "updated_at");
	}

	@Test
	void shouldLoadSeedUsersWithHashedPasswords() {
		var seedUsers = jdbcClient.sql("""
				select email, password_hash
				from users
				where email in ('admin@bearflow.local', 'user@bearflow.local')
				order by email
				""")
			.query((rs, rowNum) -> new SeedUserRow(rs.getString("email"), rs.getString("password_hash")))
			.list();

		assertThat(seedUsers)
			.extracting(SeedUserRow::email)
			.containsExactly("admin@bearflow.local", "user@bearflow.local");
		assertThat(seedUsers)
			.extracting(SeedUserRow::passwordHash)
			.allSatisfy(passwordHash -> assertThat(passwordHash).startsWith("$2a$"));
		assertThat(seedUsers)
			.extracting(SeedUserRow::passwordHash)
			.noneMatch("password"::equals);
	}

	private record SeedUserRow(String email, String passwordHash) {
	}

}
