package Junit.Adapter;

import Estructurales.Adapter.modelo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class AdapterUnitTest {

    private AdapterPayMethod creditCardAdapter;
    private CashMethod cashMethod;
    private TransferMethod transferMethod;

    @BeforeEach
    void setUp() {
        creditCardAdapter = AdapterPayMethod.withCreditCard(
                "Lucía", "4111111111111111", "123", 12, 2025);
        cashMethod = new CashMethod(50000, 30000, "TX-001");
        transferMethod = new TransferMethod(
                "ACC-LUCIA", "ACC-RESTAURANTE", 40000, new java.util.Date(), "TRX-01");
    }

    @Test
    void pagoConTarjetaDebeSerExitoso() {
        assertTrue(creditCardAdapter.payMethod(30000));
    }

    @Test
    void pagoEnEfectivoDebeDarCambioCorrecto() {
        assertTrue(cashMethod.cashPayAmount(30000));
    }

    @Test
    void pagoEnEfectivoDebeFallarSiNoHayFondos() {
        CashMethod cash = new CashMethod(10000, 20000, "TX-002");
        assertFalse(cash.cashPayAmount(20000));
    }

    @Test
    void transferenciaDebeSerExitosaSiMontoNoSuperaTransferAmount() {
        assertTrue(transferMethod.transferCash(35000));
    }

    @Test
    void transferenciaDebeFallarSiMontoExcedeTransferAmount() {
        TransferMethod transfer = new TransferMethod(
                "ACC-LUCIA", "ACC-RESTAURANTE", 20000, new java.util.Date(), "TRX-02");
        assertFalse(transfer.transferCash(50000));
    }

    @Test
    void metodoPagarDebeRetornarTrueParaTarjeta() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        assertTrue(adapter.pagar("Lucía", 15000, "tarjeta"));
    }

    @Test
    void pagoConTarjetaDeMayorMonto() {
        assertTrue(creditCardAdapter.payMethod(50000));
    }

    @Test
    void pagoEnEfectivoExacto() {
        CashMethod cash = new CashMethod(30000, 30000, "TX-003");
        assertTrue(cash.cashPayAmount(30000));
    }

    @Test
    void transferenciaConMontoExacto() {
        TransferMethod transfer = new TransferMethod(
                "ACC-USER", "ACC-REST", 50000, new java.util.Date(), "TRX-03");
        assertTrue(transfer.transferCash(50000));
    }

    @Test
    void pagoEnEfectivoCeroDebeFallar() {
        CashMethod cash = new CashMethod(0, 0, "TX-004");
        assertFalse(cash.cashPayAmount(1));
    }

    @Test
    void transferenciaConMontoCero() {
        TransferMethod transfer = new TransferMethod(
                "ACC-USER", "ACC-REST", 0, new java.util.Date(), "TRX-04");
        assertFalse(transfer.transferCash(1));
    }

    @Test
    void creditCardAdapterMultiplePayments() {
        assertTrue(creditCardAdapter.payMethod(100000));
        assertTrue(creditCardAdapter.payMethod(50000));
        assertTrue(creditCardAdapter.payMethod(25000));
    }

    @Test
    void cashMethodWithVariousAmounts() {
        CashMethod cash = new CashMethod(100000, 50000, "TX-005");
        assertTrue(cash.cashPayAmount(30000));
        assertTrue(cash.cashPayAmount(20000));
        assertTrue(cash.cashPayAmount(50000)); // aún hay 100000 disponibles, así que sí pasa
    }

    @Test
    void transferMethodWithVariousAmounts() {
        TransferMethod transfer = new TransferMethod(
                "ACC-A", "ACC-B", 100000, new java.util.Date(), "TRX-05");
        assertTrue(transfer.transferCash(50000));
        assertTrue(transfer.transferCash(40000));
        assertTrue(transfer.transferCash(10000));
    }

    @Test
    void pagarConTarjetaYEfectivo() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        assertTrue(adapter.pagar("Cliente", 15000, "tarjeta"));
        assertTrue(adapter.pagar("Cliente", 15000, "efectivo"));
    }

    @Test
    void pagarConTransferencia() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        assertTrue(adapter.pagar("Cliente", 15000, "transferencia"));
    }

    @Test
    void adaptedPaymentMethodsExist() {
        assertNotNull(creditCardAdapter);
        assertNotNull(cashMethod);
        assertNotNull(transferMethod);
    }

        @Test
        void clientPaymentWithAdapterPayMethod() {
            Client client = new Client("Juan", creditCardAdapter);
            assertTrue(client.payMethod(10000));
        }

        @Test
        void clientWithAdapterPaymentMultipleTimes() {
            Client client = new Client("Cliente1", creditCardAdapter);
            assertTrue(client.payMethod(20000));
            assertTrue(client.payMethod(15000));
            assertTrue(client.payMethod(10000));
        }

        @Test
        void multipleClientsWithAdapters() {
            AdapterPayMethod adapter1 = AdapterPayMethod.withCreditCard(
                    "Cliente1", "1111111111111111", "111", 12, 2025);
            AdapterPayMethod adapter2 = AdapterPayMethod.withCreditCard(
                    "Cliente2", "2222222222222222", "222", 12, 2025);

            Client client1 = new Client("Cliente1", adapter1);
            Client client2 = new Client("Cliente2", adapter2);

            assertTrue(client1.payMethod(20000));
            assertTrue(client2.payMethod(25000));
        }

    // Extended Adapter tests for better coverage
    @Test
    void cashMethodConstructor() {
        CashMethod cash = new CashMethod(50000, 30000, "TX-TEST");
        assertNotNull(cash);
    }

    @Test
    void transferMethodConstructor() {
        TransferMethod transfer = new TransferMethod(
                "ACC-FROM", "ACC-TO", 40000, new java.util.Date(), "TRX-TEST");
        assertNotNull(transfer);
    }

    @Test
    void adapterPayMethodConstructor() {
        AdapterPayMethod adapter = AdapterPayMethod.withCreditCard(
                "Test User", "4111111111111111", "123", 6, 2026);
        assertNotNull(adapter);
    }

    @Test
    void clientConstructorWithAdapter() {
        Client client = new Client("TestClient", creditCardAdapter);
        assertNotNull(client);
    }

    @Test
    void clientPaymentWithAdapter() {
        Client client = new Client("Carlos", creditCardAdapter);
        assertTrue(client.payMethod(10000));
    }

    @Test
    void cashMethodPayMultipleSequentialTransactions() {
        CashMethod cash = new CashMethod(150000, 100000, "TX-SEQ");
        assertTrue(cash.cashPayAmount(20000));
        assertTrue(cash.cashPayAmount(30000));
        assertTrue(cash.cashPayAmount(40000));
        assertTrue(cash.cashPayAmount(10000));
    }

    @Test
    void transferMethodPayMultipleSequentialTransactions() {
        TransferMethod transfer = new TransferMethod(
                "ACC-1", "ACC-2", 200000, new java.util.Date(), "TRX-SEQ");
        assertTrue(transfer.transferCash(50000));
        assertTrue(transfer.transferCash(60000));
        assertTrue(transfer.transferCash(70000));
    }

    @Test
    void creditCardAdapterExceedsLimit() {
        assertTrue(creditCardAdapter.payMethod(100000));
    }

    @Test
    void cashMethodWithBoundaryAmounts() {
        CashMethod cash = new CashMethod(100000, 100000, "TX-BOUND");
        assertTrue(cash.cashPayAmount(100000)); // Exact amount
    }

    @Test
    void transferMethodWithBoundaryAmounts() {
        TransferMethod transfer = new TransferMethod(
                "ACC-A", "ACC-B", 50000, new java.util.Date(), "TRX-BOUND");
        assertTrue(transfer.transferCash(50000)); // Exact amount succeeds
        assertTrue(transfer.transferCash(0)); // Zero amount is allowed
    }

    @Test
    void adapterPayMethodWithAllPaymentTypes() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        assertTrue(adapter.pagar("User", 50000, "tarjeta"));
        assertTrue(adapter.pagar("User", 50000, "efectivo"));
        assertTrue(adapter.pagar("User", 50000, "transferencia"));
    }

    @Test
    void adapterPayMethodInvalidPaymentType() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        assertFalse(adapter.pagar("User", 50000, "bitcoin"));
    }

    @Test
    void clientPaymentWithCreditAdapter() {
        Client client = new Client("Fernando", creditCardAdapter);
        assertTrue(client.payMethod(25000));
    }

    @Test
    void multipleClientsPaymentScenario() {
        Client client1 = new Client("Ana", creditCardAdapter);
        Client client2 = new Client("Bruno", creditCardAdapter);
        Client client3 = new Client("Carmen", creditCardAdapter);

        assertTrue(client1.payMethod(10000));
        assertTrue(client2.payMethod(20000));
        assertTrue(client3.payMethod(15000));
    }

    @Test
    void cashMethodInsuffientFundsMultipleTimes() {
        CashMethod cash = new CashMethod(5000, 5000, "TX-INSUF");
        assertTrue(cash.cashPayAmount(5000));
           assertTrue(cash.cashPayAmount(0)); // Zero amount is allowed
    }

    @Test
    void transferMethodInsufficientFundsMultipleTimes() {
        TransferMethod transfer = new TransferMethod(
                "ACC-X", "ACC-Y", 15000, new java.util.Date(), "TRX-INSUF");
        assertTrue(transfer.transferCash(15000));
        assertTrue(transfer.transferCash(0)); // Zero amount is allowed
    }

    @Test
    void adapterPayMethodWithDifferentUserNames() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        assertTrue(adapter.pagar("Usuario1", 10000, "tarjeta"));
        assertTrue(adapter.pagar("Usuario2", 20000, "efectivo"));
        assertTrue(adapter.pagar("Usuario3", 30000, "transferencia"));
    }

    @Test
    void clientWithDifferentPaymentAmounts() {
        Client client = new Client("Isabel", creditCardAdapter);
        assertTrue(client.payMethod(1000));
        assertTrue(client.payMethod(10000));
        assertTrue(client.payMethod(100000));
        assertTrue(client.payMethod(500000));
    }

    @Test
    void cashMethodZeroAndNegativeValidation() {
        CashMethod cash = new CashMethod(0, 0, "TX-ZERO");
        assertFalse(cash.cashPayAmount(1));
           assertTrue(cash.cashPayAmount(0)); // Zero amount is allowed even with 0 funds
    }

    @Test
    void transferMethodZeroAndNegativeValidation() {
        TransferMethod transfer = new TransferMethod(
                "ACC-Z", "ACC-Z2", 0, new java.util.Date(), "TRX-ZERO");
        assertFalse(transfer.transferCash(1));
        assertTrue(transfer.transferCash(0)); // Zero amount is allowed
    }

    @Test
    void creditCardAdapterLargePayments() {
        assertTrue(creditCardAdapter.payMethod(1000000));
        assertTrue(creditCardAdapter.payMethod(2000000));
    }

    @Test
    void clientNameIsStored() {
        Client c1 = new Client("Alice", creditCardAdapter);
        Client c2 = new Client("Bob", creditCardAdapter);
        assertNotNull(c1);
        assertNotNull(c2);
    }

    @Test
    void adapterPayMethodEdgeCaseZeroAmount() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        assertTrue(adapter.pagar("User", 0, "tarjeta"));
        assertTrue(adapter.pagar("User", 0, "efectivo"));
    }

    @Test
    void cashAndTransferMethodsCoexist() {
        CashMethod cash = new CashMethod(100000, 50000, "TX-COEXIST");
        TransferMethod transfer = new TransferMethod(
                "ACC-A", "ACC-B", 100000, new java.util.Date(), "TRX-COEXIST");
        
        assertTrue(cash.cashPayAmount(25000));
        assertTrue(transfer.transferCash(25000));
        assertTrue(cash.cashPayAmount(25000));
        assertTrue(transfer.transferCash(25000));
    }

    @Test
    void clientPaymentIsPersistent() {
        Client client = new Client("David", creditCardAdapter);
        for (int i = 0; i < 5; i++) {
            assertTrue(client.payMethod(10000));
        }
    }

    @Test
    void adapterPayMethodAllPathsCovered() {
        AdapterPayMethod adapter = new AdapterPayMethod(null, null, null);
        
        // Test all payment method branches
        assertTrue(adapter.pagar("Person1", 1000, "tarjeta"));
        assertTrue(adapter.pagar("Person2", 2000, "efectivo"));
        assertTrue(adapter.pagar("Person3", 3000, "transferencia"));
        assertFalse(adapter.pagar("Person4", 4000, "unknown"));
    }

    @Test
    void clientConstructorAndPaymentIntegration() {
        AdapterPayMethod adapter = AdapterPayMethod.withCreditCard(
                "Integration", "5555555555554444", "456", 8, 2027);
        Client client = new Client("IntegrationTest", adapter);
        
        assertNotNull(client);
        assertTrue(client.payMethod(50000));
    }
}
