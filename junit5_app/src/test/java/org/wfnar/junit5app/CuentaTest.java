package org.wfnar.junit5app;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.wfnar.junit5app.exceptios.DineroInsuficienteExceptio;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;
    @BeforeEach
    void initMetodoTest(){
        this.cuenta =new Cuenta("Andres", new BigDecimal("1000.12345"));
        System.out.println("Iniciando Metodo");
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

    @Test
    @DisplayName("Probando nombre de la cuenta!!")
    void testNombreCuenta(){
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
    void testTransferirDineroCuenta() {
        Cuenta cuenta1 = new Cuenta("Felipe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("William", new BigDecimal("2500"));
        Banco banco = new Banco();
        banco.setNombre("Bancolombia");
        banco.transferir(cuenta2,cuenta1, new BigDecimal("500"));
        assertEquals("2000", cuenta2.getSaldo().toPlainString());
        assertEquals("3000", cuenta1.getSaldo().toPlainString());

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

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testSoloJdk8() {
    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void testSoloJdk17() {
    }

    @Test
    void imprimirSystemProperties(){
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches =  ".*17.*")
    void testJavaVersion(){

    }

    @Test
    @DisabledIfSystemProperty(named = "os.arch", matches =  ".*32.*")
    void testSolo64(){

    }

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches =  ".*32.*")
    void testNo64(){

    }

}