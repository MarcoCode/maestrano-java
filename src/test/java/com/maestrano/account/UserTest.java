package com.maestrano.account;

import static net.jadler.Jadler.closeJadler;
import static net.jadler.Jadler.initJadlerUsing;
import static net.jadler.Jadler.onRequest;
import static net.jadler.Jadler.port;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.maestrano.account.User.UserClient;
import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.InvalidRequestException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.ResourcesHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

import net.jadler.stubbing.server.jdk.JdkStubHttpServer;

public class UserTest {

	private Preset preset;
	private UserClient subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		initJadlerUsing(new JdkStubHttpServer());
		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.host", "http://localhost:" + port());
		preset = new Preset("test", properties);
		subject = User.client(preset);
	}

	private static final String USER_JSON = ResourcesHelper.getResource("/com/maestrano/account/user.json");
	private static final String USERS_JSON = ResourcesHelper.getResource("/com/maestrano/account/users.json");

	@Test
	public void test_all() throws MnoConfigurationException, AuthenticationException, ApiException, InvalidRequestException, IOException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/users").respond().withBody(USERS_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		List<User> users = subject.all();

		assertEquals(1, users.size());
		User user = users.get(0);

		assertUser(user);
	}

	@Test
	public void test_retrieve() throws AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("GET").havingPathEqualTo("/api/v1/account/users/usr-001").respond().withBody(USER_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		User user = subject.retrieve("usr-001");

		assertUser(user);

	}

	@Test
	public void test_create() throws IOException, AuthenticationException, ApiException, InvalidRequestException {

		onRequest().havingMethodEqualTo("POST").havingPathEqualTo("/api/v1/account/users").respond().withBody(USER_JSON).withStatus(200).withEncoding(Charset.forName("UTF-8"))
				.withContentType("application/json; charset=UTF-8");

		Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("name", "John");
		hash.put("surname", "Smith");
		hash.put("email", "john.smith@gmail.com");

		User user = subject.create(hash);

		assertUser(user);

	}

	@Test
	public void test_delete() throws AuthenticationException, ApiException, IOException {
		onRequest().havingMethodEqualTo("DELETE").havingPathEqualTo("/api/v1/account/users/usr-001").respond().withBody(USER_JSON).withStatus(200)
				.withEncoding(Charset.forName("UTF-8")).withContentType("application/json; charset=UTF-8");

		User user = subject.delete("usr-001");

		assertUser(user);
	}

	private static void assertUser(User user) {
		assertEquals("usr-001", user.getId());
		assertEquals("John", user.getName());
		assertEquals("Smith", user.getSurname());
		assertEquals(ResourcesHelper.parseDate("2014-05-21T00:37:34Z"), user.getCreatedAt());
		assertEquals(ResourcesHelper.parseDate("2015-03-09T06:37:28Z"), user.getUpdatedAt());
		assertEquals("john.smith@gmail.com", user.getEmail());
		assertEquals("Smith Ltd.", user.getCompanyName());
		assertEquals("UK", user.getCountry());
		assertEquals("d7kp1b5esnfgtz6xhiv9qwlja34yu8crm2o0", user.getSsoSession());
	}

	@After
	public void tearDown() {
		closeJadler();
	}
}



