package org.wfnar.junit5app;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.wfnar.junit5app.exceptios.DineroInsuficienteExceptio;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;
    private TestReporter testReporter;
    private TestInfo testInfo;
    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter){
        this.cuenta =new Cuenta("Andres", new BigDecimal("1000.12345"));
        System.out.println("Iniciando Metodo");
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("iniciando el metodo.");
        testReporter.publishEntry(" ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName()
                + " con las etiquetas " + testInfo.getTags());


    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test");
    }

    @Nested
    @DisplayName("Probando atributos de cuenta")
    class CuentaTestNombreSaldo{
        @Test
        @DisplayName("Probando nombre de la cuenta!!")
        void testNombreCuenta(){

            testReporter.publishEntry(testInfo.getTags().toString());
            if (testInfo.getTags().contains("cuenta")) {
                testReporter.publishEntry("hacer algo con la etiqueta cuenta");
            }
            // cuenta.setPersona("Andres");
            String esperado ="Andres";
            String real = cuenta.getPersona();
            assertNotNull(real, () -> "LA cuenta no puede ser nula");
            assertEquals(esperado,real, () -> "El nombre de la cuenta no es el que se esperaba");
            assertTrue(real.equals("Andres"), () -> "Nombre de cueta esperada debe ser igual al real ");
        }

        @Test
        @DisplayName("Probando Saldo de la cuenta!!")
        void testSaldoCuenta(){
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertNotNull(cuenta.getSaldo());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }
        @Test
        @DisplayName("Probando referencia de la cuenta!!")
        void testReferenciaCuenta() {
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1000.12345"));
            //assertNotEquals(cuenta2,cuenta);
            assertEquals(cuenta2,cuenta);
        }
    }

    @Nested
    class CuentaOperacionesTest{
        @Test
        void debitoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        void creditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testTransferirDineroCuenta() {
            Cuenta cuenta1 = new Cuenta("Felipe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("William", new BigDecimal("2500"));
            Banco banco = new Banco();
            banco.setNombre("Bancolombia");
            banco.transferir(cuenta2,cuenta1, new BigDecimal("500"));
            assertEquals("2000", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());

        }
    }





    @Test
    void testDineroInsuficienteExceptionCuenta(){
        Cuenta cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteExceptio.class,() -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";

        assertEquals(esperado, actual);
    }



    @Test
    //@Disabled
    void testRelacionBancoCuenta() {
        //fail();
        Cuenta cuenta1 = new Cuenta("Felipe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("William", new BigDecimal("2500"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Bancolombia");

        banco.transferir(cuenta2,cuenta1, new BigDecimal("500"));
        assertEquals("2000", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());

        assertEquals(2, banco.getCuentas().size());

        assertEquals("Bancolombia", cuenta1.getBanco().getNombre());

        assertEquals("Felipe", banco.getCuentas().stream().filter(c -> c.getPersona().equals("Felipe")).findFirst().get().getPersona());
        assertTrue( banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("William")));

    }

    @Test
    void testRelacionBancoCuenta2() {
        Cuenta cuenta1 = new Cuenta("Felipe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("William", new BigDecimal("2500"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Bancolombia");

        banco.transferir(cuenta2,cuenta1, new BigDecimal("500"));
        assertAll(
                () -> {assertEquals("2000", cuenta2.getSaldo().toPlainString());},
                () -> {assertEquals("3000", cuenta1.getSaldo().toPlainString());},
                () -> {assertEquals(2, banco.getCuentas().size());},
                () -> {assertEquals("Bancolombia", cuenta1.getBanco().getNombre());},
                () -> {assertEquals("Felipe", banco.getCuentas().stream().filter(c -> c.getPersona().equals("Felipe")).findFirst().get().getPersona());},
                () -> {assertTrue( banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("William")));}
        );
    }

    @Nested
    class sistemaOperativo {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }

        @Test
        @DisabledOnOs({OS.LINUX, OS.MAC})
        void testNoLinuxMac() {
        }
    }

    @Nested
    class javaVeseionTest{
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testSoloJdk8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void testSoloJdk17() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_17)
        void testNoJDK15() {
        }

    }


    @Nested
    class SistemPropertiesTest{
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v)-> System.out.println(k + ":" + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*15.*")
        void testJavaVersion() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNO64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "aguzm")
        void testUsername() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }

    @Nested
    class VariableAmbienteTest{
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v)-> System.out.println(k + " = " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-15.0.1.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
        }

    }

    @Test
    @DisplayName("test Saldo Cuenta Dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev);
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("test Saldo Cuenta Dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @DisplayName("Probando Debito Cuenta Repetir!")
    @RepeatedTest(value=5, name = "{displayName} - Repetición numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition() == 3){
            System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
        }
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("param")
    @Nested
    class PruebasParametrizadasTest{

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.12345"})
        void testDebitoCuentaCsvSource(String index, String monto) {
            System.out.println(index + " -> " + monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"200,100,John,Andres", "250,200,Pepe,Pepe", "300,300,maria,Maria", "510,500,Pepa,Pepa", "750,700,Lucas,Luca", "1000.12345,1000.12345,Cata,Cata"})
        void testDebitoCuentaCsvSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoCuentaCsvFileSource2(String saldo, String monto, String esperado, String actual) {
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            cuenta.setPersona(actual);

            assertNotNull(cuenta.getSaldo());
            assertNotNull(cuenta.getPersona());
            assertEquals(esperado, actual);

            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

    }

    @Tag("param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700", "1000.12345");
    }

    @Nested
    @Tag("timeout")
    class EjemploTimeoutTest{
        @Test
        @Timeout(1)
        void pruebaTimeout() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        void testTimeoutAssertions() {
            assertTimeout(Duration.ofSeconds(5), ()->{
                TimeUnit.MILLISECONDS.sleep(4000);
            });
        }
    }

}