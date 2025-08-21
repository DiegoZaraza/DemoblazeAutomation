# Selenium Java - Demoblaze Automation 🚀

Automatización end-to-end para [Demoblaze](https://www.demoblaze.com/) usando Selenium WebDriver con Java, Maven, Page Factory y TestNG con reportes ExtentReports detallados.

## 📋 Requisitos

- **Java** 11 o superior
- **Maven** 3.6 o superior
- **Git** (para clonar el repositorio)

## 🚀 Instalación y Configuración

```bash
# Clonar el repositorio
git clone <repository-url>
cd selenium-demoblaze

# Compilar el proyecto
mvn clean compile

# Descargar dependencias
mvn dependency:resolve
```

## 🧪 Ejecución de Pruebas

### Ejecutar todas las pruebas
```bash
mvn test
```

### Ejecutar con diferentes navegadores
```bash
# Chrome (por defecto)
mvn test -Dbrowser=chrome

# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge
```

### Ejecutar en modo headless
```bash
mvn test -Dheadless=true
```

### Ejecutar perfiles específicos
```bash
# Perfil headless
mvn test -P headless

# Perfil Firefox
mvn test -P firefox
```

### Ejecutar pruebas específicas
```bash
# Solo pruebas de login
mvn test -Dtest=LoginTest

# Solo pruebas de categorías
mvn test -Dtest=CategoryTest

# Múltiples clases de prueba
mvn test -Dtest=LoginTest,CategoryTest
```

### Ejecutar métodos específicos
```bash
# Solo un método específico
mvn test -Dtest=LoginTest#testValidLogin

# Múltiples métodos
mvn test -Dtest=LoginTest#testValidLogin,LoginTest#testInvalidLogin
```

## 📁 Estructura del Proyecto

```
📦 selenium-demoblaze/
├── 📂 src/test/java/com/demoblaze/
│   ├── 📂 base/
│   │   └── BaseTest.java           # Clase base para todas las pruebas
│   ├── 📂 pages/                   # Page Object Models con Page Factory
│   │   ├── HomePage.java
│   │   ├── LoginPage.java
│   │   ├── ProductPage.java
│   │   ├── CartPage.java
│   │   └── ProfilePage.java
│   ├── 📂 tests/                   # Clases de prueba TestNG
│   │   ├── LoginTest.java
│   │   ├── CategoryTest.java
│   │   ├── PurchaseTest.java
│   │   ├── ProfileTest.java
│   │   └── ExtendedTest.java
│   ├── 📂 utils/                   # Utilidades
│   │   ├── DriverManager.java
│   │   └── ConfigReader.java
│   ├── 📂 data/                    # Datos de prueba
│   │   └── TestData.java
│   ├── 📂 model/                   # Modelos de datos
│   │   └── PurchaseData.java
│   └── 📂 listeners/               # Listeners de TestNG
│       ├── ExtentReportListener.java
│       └── ScreenshotListener.java
├── 📂 src/test/resources/
│   ├── config.properties           # Configuración
│   ├── log4j2.xml                 # Configuración de logging
│   └── testng.xml                 # Suite de TestNG
├── 📂 reports/                    # Reportes generados
│   ├── screenshots/               # Capturas en fallos
│   └── *.html                     # Reportes ExtentReports
├── pom.xml                        # Configuración Maven
└── README.md
```

## 🎯 Cobertura de Pruebas

### ✅ Parte 1: Setup Básico
- **Configuración Maven**: POM con todas las dependencias
- **Page Factory**: Implementación completa con Page Object Model
- **Configuración**: Properties, logging, y manejo de drivers
- **Documentación**: README detallado

### ✅ Parte 2: Casos Esenciales

#### 🔐 Login (LoginTest.java)
- ✅ Login válido con admin/admin
- ✅ Login inválido con credenciales incorrectas
- ✅ Manejo de modales y alerts
- ✅ Validación de estados de sesión

#### 📁 Categorías (CategoryTest.java)
- ✅ Verificar existencia de: Phones, Laptops, Monitors
- ✅ Validar productos por categoría
- ✅ Verificar productos únicos por categoría
- ✅ Consistencia de navegación

#### 🛒 Compra Completa (PurchaseTest.java)
- ✅ Flujo end-to-end: Producto → Carrito → Checkout
- ✅ Validación de totales
- ✅ Confirmación de pedido con ID
- ✅ Múltiples productos y categorías

#### 👤 Perfil (ProfileTest.java)
- ✅ Simulación con localStorage
- ✅ Persistencia entre recargas
- ✅ Actualización parcial de datos
- ✅ Ciclo de vida completo

### ✅ Parte 3: Casos Extendidos (ExtendedTest.java)

#### ❌ Caso Negativo
- ✅ Checkout sin productos → Validación de error
- ✅ Manejo adecuado de estados vacíos

#### 🔢 Validación Dinámica
- ✅ Mismo producto múltiples veces (simula cantidad)
- ✅ Actualización correcta de totales
- ✅ Validación de cálculos

### ✅ Parte 4: Bonus - Reportes Automatizados

#### 📊 ExtentReports
- ✅ Reportes HTML detallados con timestamp
- ✅ Categorización por tipo de prueba
- ✅ Información del sistema y navegador
- ✅ Logs detallados por paso

#### 📸 Screenshots Automáticos
- ✅ Captura automática solo en fallos
- ✅ Integración con reportes ExtentReports
- ✅ Nombres únicos con timestamp

#### 📝 Logging Avanzado
- ✅ Log4j2 con múltiples appenders
- ✅ Logs rotativos por tamaño y fecha
- ✅ Niveles configurables por paquete

## ⚙️ Configuración

### Variables de Entorno
```bash
# Configurar navegador
export BROWSER=chrome

# Modo headless
export HEADLESS=true

# URL base personalizada
export BASE_URL=https://www.demoblaze.com/

# Timeouts personalizados
export TIMEOUT=15
export IMPLICIT_WAIT=10
```

### Archivo config.properties
```properties
# Personaliza la configuración en src/test/resources/config.properties
base.url=https://www.demoblaze.com/
browser=chrome
headless=false
timeout=10
implicit.wait=5
```

## 🔧 Tecnologías y Patrones

### 🏗️ Arquitectura
- **Page Object Model**: Separación clara de lógica de páginas
- **Page Factory**: Inicialización automática de elementos con @FindBy
- **Base Test**: Configuración común y manejo de drivers
- **Data Provider**: Datos de prueba centralizados

### 📚 Dependencias Principales
- **Selenium WebDriver** 4.18.1: Automatización web
- **TestNG** 7.9.0: Framework de pruebas
- **WebDriverManager** 5.7.0: Gestión automática de drivers
- **ExtentReports** 5.0.9: Reportes HTML avanzados
- **Log4j2** 2.22.1: Logging robusto
- **Maven** 3.x: Gestión de dependencias y build

### 🎨 Patrones Implementados
- **Singleton**: DriverManager para instancia única de WebDriver
- **Factory**: Creación de drivers por tipo de navegador
- **Builder**: PurchaseData para construcción de objetos
- **Observer**: Listeners para eventos de TestNG

## 🐛 Debugging y Troubleshooting

### Logs Detallados
```bash
# Ver logs en tiempo real
tail -f reports/demoblaze-tests.log

# Logs de ejecución específica
cat reports/test-execution.log
```

### Screenshots de Fallos
Las capturas se guardan automáticamente en `reports/screenshots/` con formato:
```
ClassName_TestMethod_YYYY-MM-DD_HH-mm-ss.png
```

### Modo Debug
```bash
# Ejecutar con logs debug
mvn test -Dlog.level=DEBUG

# Mantener navegador abierto en fallos (para desarrollo)
mvn test -Dheadless=false -Dtimeout=60
```

## 🌐 Notas del Entorno Demoblaze

### ⚠️ Limitaciones Conocidas
- **Login**: Si admin/admin no funciona, se documenta como hallazgo
- **Carrito**: No permite editar cantidades nativas
- **Perfil**: Simulado con localStorage (no existe en el sitio)

### 🔄 Workarounds Implementados
- **Cantidad de productos**: Se simula agregando el mismo producto varias veces
- **Persistencia de perfil**: Usando localStorage del navegador
- **Manejo de alerts**: Captura y validación de diálogos JavaScript

## 📊 Reportes

### ExtentReports HTML
- **Ubicación**: `reports/DemoblazeTestReport_TIMESTAMP.html`
- **Contenido**:
    - Dashboard ejecutivo con métricas
    - Detalles por prueba con logs paso a paso
    - Screenshots integrados en fallos
    - Información del sistema y ambiente
    - Gráficos de resultados por categoría

### Logs Estructurados
- **Archivo principal**: `reports/demoblaze-tests.log`
- **Por ejecución**: `reports/test-execution.log`
- **Rotación automática**: Por tamaño (250MB) y fecha

## 🚀 CI/CD y Integración

### Maven Profiles
```bash
# Profile para CI/CD
mvn test -P headless

# Profile para desarrollo local
mvn test -P chrome
```

### Jenkins/GitHub Actions
```yaml
# Ejemplo para pipeline
- name: Run Tests
  run: mvn test -Dheadless=true -Dbrowser=chrome
  
- name: Archive Reports  
  uses: actions/upload-artifact@v3
  with:
    name: test-reports
    path: reports/
```

## 🤝 Contribución

1. Fork del proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Seguir convenciones de código:
    - Page Objects en `pages/`
    - Tests en `tests/`
    - Utilidades en `utils/`
4. Commit con mensajes descriptivos
5. Push y crear Pull Request

## 📝 Convenciones de Código

### Nomenclatura
- **Clases**: PascalCase (`LoginPage`, `BaseTest`)
- **Métodos**: camelCase (`testValidLogin`, `clickLoginButton`)
- **Variables**: camelCase (`usernameField`, `cartItems`)
- **Constantes**: UPPER_CASE (`VALID_USERNAME`, `BASE_URL`)

### Estructura de Tests
```java
@Test(description = "Descripción clara del caso de prueba")
public void testMethodName() {
    // Arrange - Configurar datos y estado inicial
    
    // Act - Ejecutar la acción que se está probando
    
    // Assert - Verificar el resultado esperado
}
```

---

## 📋 Checklist de Requisitos Cumplidos

- ✅ **Java + Maven + Page Factory**: Implementación completa
- ✅ **Parte 1**: Setup, dependencias, config y README detallado
- ✅ **Parte 2**: Login (±), categorías, compra completa, perfil simulado
- ✅ **Parte 3**: Checkout sin producto, validación de totales dinámicos
- ✅ **Parte 4**: ExtentReports HTML + screenshots automáticos en fallos

**🎯 Proyecto listo para evaluación técnica y producción** 🎯