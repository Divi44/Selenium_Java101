# LambdaTest Selenium Test Automation Suite

This repository demonstrates parallel test automation using Java, Selenium WebDriver, TestNG, and LambdaTest's cloud grid.  
Three test scenarios illustrate different Selenium Playground features, accompanied by TestNG XML suite files for parallel execution.

---

## Table of Contents

- [Overview](#overview)
- [Test Scenarios](#test-scenarios)
  - [Scenario 1: Simple Form Demo](#scenario-1-simple-form-demo)
  - [Scenario 2: Drag & Drop Sliders](#scenario-2-drag--drop-sliders)
  - [Scenario 3: Input Form Submit](#scenario-3-input-form-submit)
- [TestNG Suite Files](#testng-suite-files)
- [Running the Tests](#running-the-tests)
- [Environment Setup](#environment-setup)
- [Notes](#notes)

---

## Overview

This project contains Java test classes that automate UI testing on [LambdaTest Selenium Playground](https://www.lambdatest.com/selenium-playground) using the LambdaTest Selenium Grid.  
Tests are written with TestNG and utilize LambdaTest’s parallel execution, test result reporting, and cloud dashboard.

---

## Test Scenarios

### Scenario 1: Simple Form Demo (`Test_Scenario1.java`)

- Navigates to Selenium Playground.
- Opens **"Simple Form Demo"**.
- Enters a custom message and verifies it is displayed correctly using UI automation.
- Marks the test status as passed in LambdaTest.

### Scenario 2: Drag & Drop Sliders (`Test_Scenario2.java`)

- Navigates to Selenium Playground.
- Opens **"Drag & Drop Sliders"**.
- Sets the slider with initial value "15" to "95" programmatically.
- Asserts that the displayed slider value is updated to "95".
- Marks the test status as passed in LambdaTest.

### Scenario 3: Input Form Submit (`Test_Scenario3.java`)

- Navigates to Selenium Playground.
- Opens **"Input Form Submit"**.
- Validates that browser-required field warnings appear if the form is submitted empty.
- Fills in all required fields, selects a country, and submits.
- Asserts that the post-submit success message appears.
- Marks the test status as passed in LambdaTest.

---

## TestNG Suite Files

- **`TS_1.xml`, `TS_2.xml`, `TS_3.xml`**  
  TestNG suite files for executing these tests in parallel, with examples for running specific or multiple scenarios concurrently.
  - They include sample thread-counts and placeholder classes for further test extension.

---

## Running the Tests

### Prerequisites

1. **Java** (JDK 8+)
2. **Maven** or your preferred Java build tool
3. **TestNG** dependency in your project
4. **Selenium WebDriver** dependencies
5. **LambdaTest account** credentials

### Environment Variables

You must set the following environment variables (export in your shell, or set in your CI/CD):

- `LT_USERNAME` : Your LambdaTest username  
- `LT_ACCESS_KEY` : Your LambdaTest access key

### Example Command

```bash
export LT_USERNAME=your_lambdatest_username
export LT_ACCESS_KEY=your_lambdatest_access_key

# To run a suite (using Maven's surefire plugin)
mvn test -DsuiteXmlFile=TS_1.xml