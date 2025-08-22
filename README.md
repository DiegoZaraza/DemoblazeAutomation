# Selenium Java - Demoblaze Automation 🚀

End-to-end automation for [Demoblaze](https://www.demoblaze.com/) using Selenium WebDriver with Java, Maven, Page Factory and TestNG with detailed ExtentReports.

## 📋 Requirements

- **Java** 11 or higher
- **Maven** 3.6 or higher
- **Git** (to clone the repository)

## 🚀 Installation and Setup

```bash
# Clone repository
git clone <repository-url>
cd selenium-demoblaze

# Compile project
mvn clean compile

# Download dependencies
mvn dependency:resolve

# Install Allure CLI (optional for enhanced reporting)
# For macOS with Homebrew
brew install allure

# For Windows with Scoop
scoop install allure

# For Linux
wget https://github.com/allure-framework/allure2/releases/download/2.24.0/allure-2.24.0.tgz
tar -zxvf allure-2.24.0.tgz
sudo mv allure-2.24.0 /opt/allure
export PATH=$PATH:/opt/allure/bin
```

## 🧪 Test Execution

### Run all tests
```bash
mvn test
```

### Run with different browsers
```bash
# Chrome (default)
mvn test -Dbrowser=chrome

# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge
```

### Run in headless mode
```bash
mvn test -Dheadless=true
```

### Run specific tests
```bash
# Login tests only
mvn test -Dtest=LoginTest

# Category tests only
mvn test -Dtest=CategoryTest

# Multiple test classes
mvn test -Dtest=LoginTest,CategoryTest
```

### Run specific methods
```bash
# Single specific method
mvn test -Dtest=LoginTest#testValidLogin

# Multiple methods
mvn test -Dtest=LoginTest#testValidLogin,LoginTest#testInvalidLogin
```
### Allure Reports (Enhanced Interactive Reports)

#### Generate Allure Results
```bash
# Run tests to generate Allure results
mvn clean test

# Allure results are saved in: target/allure-results/
```

#### View Allure Report
```bash
# Method 1: Generate and serve report locally
allure serve target/allure-results

# Method 2: Generate report to specific directory
allure generate target/allure-results --output target/allure-report --clean

# Method 3: Open generated report
allure open target/allure-report
```

#### Allure with Maven
```bash
# Generate Allure report using Maven plugin
mvn allure:report

# Serve Allure report using Maven
mvn allure:serve
```

## 📁 Project Structure

```
📦 selenium-demoblaze/
├── 📂 src/test/java/com/demoblaze/
│   ├── 📂 base/
│   │   └── BaseTest.java           # Base class for all tests
│   ├── 📂 pages/                   # Page Object Models with Page Factory
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   ├── ProductPage.java
│   │   ├── CartPage.java
│   │   └── ProfilePage.java
│   ├── 📂 tests/                   # TestNG test classes
│   │   ├── LoginTest.java
│   │   ├── CategoryTest.java
│   │   ├── PurchaseTest.java
│   │   ├── ProfileTest.java
│   │   └── ExtendedTest.java
│   ├── 📂 utils/                   # Utilities
│   │   ├── DriverManager.java
│   │   └── ConfigReader.java
│   ├── 📂 data/                    # Test data
│   │   └── TestData.java
│   ├── 📂 model/                   # Data models
│   │   └── PurchaseData.java
│   └── 📂 listeners/               # TestNG listeners
│       ├── ExtentReportListener.java
│       └── ScreenshotListener.java
├── 📂 src/test/resources/
│   ├── config.properties           # Configuration
│   ├── log4j2.xml                 # Logging configuration
│   └── testng.xml                 # TestNG suite
├── 📂 reports/                    # Generated reports
│   ├── screenshots/               # Failure screenshots
│   └── *.html                     # ExtentReports
├── pom.xml                        # Maven configuration
└── README.md
```

## 🎯 Test Coverage

### ✅ Part 1: Basic Setup
- **Maven Configuration**: POM with all dependencies
- **Page Factory**: Complete implementation with Page Object Model
- **Configuration**: Properties, logging, and driver management
- **Documentation**: Detailed README

### ✅ Part 2: Essential Test Cases

#### 🔐 Login (LoginTest.java)
- ✅ Valid login with admin/admin
- ✅ Invalid login with incorrect credentials

#### 📁 Categories (CategoryTest.java)
- ✅ Verify existence of: Phones, Laptops, Monitors
- ✅ Validate products by category

#### 🛒 Complete Purchase (PurchaseTest.java)
- ✅ End-to-end flow: Product → Cart → Checkout

#### 👤 Profile (ProfileTest.java)
- ✅ Persistence between reloads

### ✅ Part 3: Extended Cases (ExtendedTest.java)

#### ❌ Negative Case
- ✅ Checkout without products → Error validation

#### 🔢 Dynamic Validation
- ✅ Calculation validation

### ✅ Part 4: Bonus - Automated Reports

#### 📊 ExtentReports
- ✅ Detailed HTML reports with timestamp
- ✅ Categorization by test type
- ✅ System and browser information
- ✅ Detailed step-by-step logs

#### 📸 Automatic Screenshots
- ✅ Automatic capture only on failures
- ✅ Integration with ExtentReports
- ✅ Unique names with timestamp

#### 📝 Advanced Logging
- ✅ Log4j2 with multiple appenders
- ✅ Rolling logs by size and date
- ✅ Configurable levels per package

## License
This project is licensed under the MIT License. See the LICENSE file for details.