package com.automation.stepDefinitions;

import java.io.IOException;
import java.text.MessageFormat;

import com.automation.model.request.CreateCar;
import com.automation.model.request.CreateUser;
import com.automation.model.request.UpdateCar;
import com.automation.model.response.CreateCarResp;
import com.automation.model.response.CreateUserResp;
import com.automation.model.response.GetCarResp;
import com.automation.model.response.GetUserResp;
import com.automation.model.response.UpdateCarResp;
import com.automation.utils.Utilities;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;

@ScenarioScoped
public class E2ETest {

	private static Response response;
	private static Response responsecarGet, responsecarUpdatedGet;
	private static Response responseusrGet;
	private static Response responseCreateCar;
	private static Response responseupdateCar;
	private static Response responsedelCar, responsedelCartwice;
	private static Response responsedelUsr, responsedelUsrtwice;
	private CreateUser createUsr;
	private CreateCar createCar;
	private UpdateCar updateCar;
	private CreateUserResp createUsrResponse;
	private CreateCarResp createCarResponse;
	private GetUserResp getusrResp;
	private GetCarResp getcarResp, getcarUpdatedResp;
	private UpdateCarResp updatecarResp;
	Utilities util = new Utilities("Config.properties");
	private SoftAssert softAssertion = new SoftAssert();
	Scenario scenario;
	private String testName, updatedManufactuer, updatedModel, updatedimageurl;

	@Before
	public void before(Scenario scenario) {
		this.scenario = scenario;

	}

	@Given("I hit the baseURL and port of the API")
	public void i_hit_the_base_url_and_port_of_the_api() {
		try {
			testName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			RestAssured.baseURI = util.getInputdata("base_url") + ":"
					+ Integer.parseInt(util.getInputdata("base_port"));
			// RestAssured.urlEncodingEnabled = false;
			Reporter.log(testName + " :: Successful formation of BaseURI", true);

		} catch (Exception e) {
			Reporter.log(testName + " :: Unable to form the BaseURI", true);
			softAssertion.fail(testName + " :: " + e.getMessage());
		}
	}

	@When("I create and verify an user with {string} and an {string}")
	public void i_create_and_verify_an_user_with_and_an(String name, String email) throws IOException {
		try {
			testName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			String updated_email = util.uniqueEmailidCreation(email);
			createUsr = new CreateUser(updated_email, name);
			response = RestAssured.given().header("Content-Type", "application/json").body(createUsr)
					.post(util.getInputdata("create_new_user_call"));
			try {
				createUsrResponse = response.getBody().as(CreateUserResp.class);
				Reporter.log(testName + " :: Successful Create user response schema validation", true);

				try {

					Assert.assertEquals(createUsrResponse.email, updated_email);
					Reporter.log(testName + " :: Successful User Email verification", true);

				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: User email verification Failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());

				}
				try {
					Assert.assertEquals(createUsrResponse.name, name);
					Reporter.log(testName + " :: Successful User Name verification", true);

				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: User name verification failed", true);
					softAssertion.fail(assertErr.getMessage());

				}
			} catch (Exception e) {
				Reporter.log(testName + " :: Create user response schema validation Failed", true);
				softAssertion.fail(testName + " :: " + e.getMessage());

			}
			try {
				Assert.assertEquals(response.getStatusCode(), 201, testName + response.getStatusLine());
				Reporter.log(testName + " :: OK " + response.getStatusLine(), true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: [ERROR] in Status code :: " + assertErr.getMessage(), true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());

			}
		} catch (Exception e) {
			Reporter.log(testName + " :: " + e.getMessage(), true);
			softAssertion.fail(testName + "  :: " + e.getMessage());
		}
	}

	@When("I Get and verify the Created user details")
	public void i_get_and_verify_the_created_user_details() {
		try {
			testName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			responseusrGet = RestAssured.given().header("Content-Type", "application/json")
					.get(MessageFormat.format(util.getInputdata("user_operation_calls"), createUsrResponse.id));
			try {
				getusrResp = responseusrGet.getBody().as(GetUserResp.class);
				Reporter.log(testName + " :: Successful Get User response schema validation", true);

				try {
					Assert.assertEquals(getusrResp.email, createUsr.getEmail());
					Reporter.log(testName + " :: successful Get User email verification", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: Get User email verification failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());

				}
				try {
					Assert.assertEquals(getusrResp.name, createUsr.getName());
					Reporter.log(testName + " :: successful Get UserName verification", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: Get UserName verification failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());

				}
			} catch (Exception e) {
				Reporter.log(testName + " :: Get User response schema validation failed", true);
				softAssertion.fail(testName + " :: " + e.getMessage());

			}
			try {
				Assert.assertEquals(responseusrGet.getStatusCode(), 200, testName + response.getStatusLine());
				Reporter.log(testName + " :: OK " + response.getStatusLine(), true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: [ERROR] in Status code :: " + assertErr.getMessage(), true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());

			}
		} catch (Exception e) {
			Reporter.log(testName + " :: " + e.getMessage(), true);
			softAssertion.fail(testName + "  :: " + e.getMessage());
		}
	}

	@When("I create car of {string} and a {string} and {string} with and verify it")
	public void i_create_car_of_and_a_and_with_and_verify_it(String manufacturer, String model, String imageUrl) {

		try {
			testName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			createCar = new CreateCar(manufacturer, model, imageUrl, createUsrResponse.id);
			responseCreateCar = RestAssured.given().header("Content-Type", "application/json").body(createCar)
					.post(util.getInputdata("create_new_car_call"));
			try {
				createCarResponse = responseCreateCar.getBody().as(CreateCarResp.class);
				Reporter.log(testName + " :: Successful create Car response schema validation", true);

				try {
					Assert.assertEquals(createCarResponse.manufacture, manufacturer);
					Reporter.log(testName + " :: Successful create car manufacturer validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: create car manufacturer validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());

				}
				try {
					Assert.assertEquals(createCarResponse.model, model);
					Reporter.log(testName + " :: Successful create car model validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: create car model validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					Assert.assertEquals(createCarResponse.imageUrl, imageUrl);
					Reporter.log(testName + " :: Successful create car imageUrl validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: create car imageUrl validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					Assert.assertEquals(createCarResponse.userId, createUsrResponse.id);
					Reporter.log(testName + " :: successful user of newly created car validation", true);

				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: user validation of newly created car failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
			} catch (Exception e) {
				Reporter.log(testName + " :: create Car response schema validation failed", true);
				softAssertion.fail(testName + " :: " + e.getMessage());

			}
			try {
				Assert.assertEquals(responseCreateCar.getStatusCode(), 201,
						testName + " :: Status code validation successful :: " + responseCreateCar.getStatusLine());
				Reporter.log(testName + " :: Status code validation successful :: " + response.getStatusLine(), true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: [Error] :: status code validation failed " + response.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}
		} catch (Exception e) {
			Reporter.log(testName + " :: " + e.getMessage(), true);
			softAssertion.fail(testName + "  :: " + e.getMessage());
		}
	}

	@When("I Get and verify the car details")
	public void i_get_and_verify_the_car_details() {
		try {
			testName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			responsecarGet = RestAssured.given().header("Content-Type", "application/json")
					.get(MessageFormat.format(util.getInputdata("car_operation_calls"), createCarResponse.id));
			try {
				getcarResp = responsecarGet.getBody().as(GetCarResp.class);
				Reporter.log(testName + " :: successful Get car details schema validation", true);

				try {
					Assert.assertEquals(getcarResp.getManufacture(), createCarResponse.manufacture);
					Reporter.log(testName + " :: Successful get car manufacturer validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get car manufacturer validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					Assert.assertEquals(getcarResp.getModel(), createCarResponse.model);
					Reporter.log(testName + " :: Successful get car model validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get car model validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					Assert.assertEquals(getcarResp.getImageUrl(), createCarResponse.imageUrl);
					Reporter.log(testName + " :: Successful get car imageUrl validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get car imageurl validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					Assert.assertEquals(getcarResp.getUserId(), createCarResponse.userId);
					Reporter.log(testName + " :: Successful get user of the car validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get user of a car validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
			} catch (Exception e) {
				Reporter.log(testName + " :: Get car details schema validation failed", true);
				softAssertion.fail(testName + " :: " + e.getMessage());

			}
			try {
				Assert.assertEquals(responsecarGet.getStatusCode(), 200,
						testName + " :: Status code validation successful :: " + responsecarGet.getStatusLine());
				Reporter.log(testName + " :: Status code validation successful :: " + response.getStatusLine(), true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: [Error] :: status code validation failed " + response.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}

		} catch (

		Exception e) {
			Reporter.log(testName + " :: " + e.getMessage(), true);
			softAssertion.fail(testName + " :: " + e.getMessage());
		}
	}

	@When("I update the car of {string} with {string} and {string} verify the car details")
	public void i_update_the_car_of_with_and_verify_the_car_details(String updatedmanufactuer, String updatedmodel,
			String updatedimageUrl) {
		try {
			testName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			updateCar = new UpdateCar(updatedmanufactuer, updatedmodel, updatedimageUrl);
			responseupdateCar = RestAssured.given().header("Content-Type", "application/json").body(updateCar)
					.put(MessageFormat.format(util.getInputdata("car_operation_calls"), createCarResponse.id));

			try {
				updatecarResp = responseupdateCar.getBody().as(UpdateCarResp.class);
				Reporter.log(testName + " :: Successful update Car response schema validation", true);

				try {
					softAssertion.assertEquals(updatecarResp.getManufacture(), updatedmanufactuer);
					Reporter.log(testName + " :: Successful update car manufacturer validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: update car manufacturer validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					softAssertion.assertEquals(updatecarResp.getModel(), updatedmodel);
					Reporter.log(testName + " :: Successful update car model validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: update car model validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					softAssertion.assertEquals(updatecarResp.getImageUrl(), updatedimageUrl);
					Reporter.log(testName + " :: Successful update car imageUrl validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: update car imageUrl validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					softAssertion.assertEquals(updatecarResp.getUserId(), createCarResponse.userId);
					Reporter.log(testName + " :: successful user validation of updated car data", true);

				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: user validation of after updateding car data failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}

			} catch (Exception e) {
				Reporter.log(testName + " :: update Car response schema validation failed", true);
				softAssertion.fail(testName + " :: " + e.getMessage());

			}

			try {
				Assert.assertEquals(responseupdateCar.getStatusCode(), 200,
						testName + " :: Status code validation successful :: " + responsecarGet.getStatusLine());
				Reporter.log(testName + " :: Status code validation successful :: " + response.getStatusLine(), true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: [Error] :: status code validation failed " + response.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}
		} catch (Exception e) {
			Reporter.log(testName + " :: " + e.getMessage(), true);
			softAssertion.fail(testName + " :: " + e.getMessage());
		}
	}

	@When("I get and verify the car details again")
	public void i_get_and_verify_the_car_details_again() {
		try {
			testName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			responsecarUpdatedGet = RestAssured.given().header("Content-Type", "application/json")
					.get(MessageFormat.format(util.getInputdata("car_operation_calls"), createCarResponse.id));
			try {
				getcarUpdatedResp = responsecarUpdatedGet.getBody().as(GetCarResp.class);
				Reporter.log(testName + " :: successful Get car details schema validation", true);

				try {
					Assert.assertEquals(getcarUpdatedResp.getManufacture(), updateCar.getManufacture());
					Reporter.log(testName + " :: Successful get car manufacturer validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get car manufacturer validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					Assert.assertEquals(getcarUpdatedResp.getModel(), updateCar.getModel());
					Reporter.log(testName + " :: Successful get car model validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get car model validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					System.out.println(updateCar.getImageUrl()+ getcarUpdatedResp.getImageUrl());
					Assert.assertEquals(getcarUpdatedResp.getImageUrl(), updateCar.getImageUrl());
					Reporter.log(testName + " :: Successful get car imageUrl validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get car imageurl validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
				try {
					Assert.assertEquals(getcarUpdatedResp.getUserId(), createCarResponse.userId);
					Reporter.log(testName + " :: Successful get user of the car validation", true);
				} catch (AssertionError assertErr) {
					Reporter.log(testName + " :: get user of a car validation failed", true);
					softAssertion.fail(testName + " :: " + assertErr.getMessage());
				}
			} catch (Exception e) {
				Reporter.log(testName + " :: Get car details schema validation failed", true);
				softAssertion.fail(testName + " :: " + e.getMessage());

			}
			try {
				Assert.assertEquals(responsecarGet.getStatusCode(), 200,
						testName + " :: Status code validation successful :: " + responsecarGet.getStatusLine());
				Reporter.log(testName + " :: Status code validation successful :: " + response.getStatusLine(), true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: [Error] :: status code validation failed " + response.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}

		} catch (

		Exception e) {
			Reporter.log(testName + " :: " + e.getMessage(), true);
			softAssertion.fail(testName + " :: " + e.getMessage());
		}
	}

	@Then("I delete both car and user and verify")
	public void i_delete_both_car_and_user_and_verify() {

		try {
			responsedelCar = RestAssured.given().header("Content-Type", "application/json")
					.delete(MessageFormat.format(util.getInputdata("car_operation_calls"), createCarResponse.id));
			// System.out.println(createCarResponse.id);
			try {
				Assert.assertEquals(responsedelCar.getStatusCode(), 204,
						testName + " :: Status code validation successful :: " + responsecarGet.getStatusLine());
				Reporter.log(testName + " :: Status code validation successful :: " + responsedelCar.getStatusLine(),
						true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: Error :: status code validation failed " + responsedelCar.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}
			responsedelUsr = RestAssured.given().header("Content-Type", "application/json")
					.delete(MessageFormat.format(util.getInputdata("user_operation_calls"), createUsrResponse.id));
			try {
				Assert.assertEquals(responsedelUsr.getStatusCode(), 204,
						testName + " :: Status code validation successful :: " + responsedelUsr.getStatusLine());
				Reporter.log(testName + " :: Status code validation successful :: " + responsedelUsr.getStatusLine(),
						true);
			} catch (AssertionError assertErr) {
				Reporter.log(testName + " :: Error :: status code validation failed " + responsedelUsr.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}
			responsedelCartwice = RestAssured.given().header("Content-Type", "application/json")
					.delete(MessageFormat.format(util.getInputdata("car_operation_calls"), createCarResponse.id));
			try {
				Assert.assertEquals(responsedelCartwice.getStatusCode(), 404,
						testName + " :: Status code validation successful :: " + responsedelCartwice.getStatusLine());
				Reporter.log(
						testName + " :: Status code validation successful :: " + responsedelCartwice.getStatusLine(),
						true);
			} catch (AssertionError assertErr) {
				Reporter.log(
						testName + " :: Error :: status code validation failed " + responsedelCartwice.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}
			responsedelUsrtwice = RestAssured.given().header("Content-Type", "application/json")
					.delete(MessageFormat.format(util.getInputdata("user_operation_calls"), createUsrResponse.id));
			try {
				Assert.assertEquals(responsedelUsr.getStatusCode(), 404,
						testName + " :: Status code validation successful :: " + responsedelUsrtwice.getStatusLine());
				Reporter.log(
						testName + " :: Status code validation successful :: " + responsedelUsrtwice.getStatusLine(),
						true);
			} catch (AssertionError assertErr) {
				Reporter.log(
						testName + " :: Error :: status code validation failed " + responsedelUsrtwice.getStatusLine(),
						true);
				softAssertion.fail(testName + " :: " + assertErr.getMessage());
			}

		} catch (Exception e) {
			Reporter.log(testName + " :: " + e.getMessage(), true);
			softAssertion.fail(testName + " :: " + e.getMessage());
		}
	}

	@After
	public void afterScenario(Scenario scenario) throws Exception {
		softAssertion.assertAll();
	}
}
