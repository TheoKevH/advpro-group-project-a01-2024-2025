package id.ac.ui.cs.advprog.buildingstore.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TransactionMetrics {

    private final MeterRegistry meterRegistry;
    private Counter totalTransactions;
    private Counter completedTransactions;

    public TransactionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        totalTransactions = meterRegistry.counter("transactions.total");
        completedTransactions = meterRegistry.counter("transactions.completed");
    }

    public void incrementTotal() {
        totalTransactions.increment();
    }

    public void incrementCompleted() {
        completedTransactions.increment();
    }
}
