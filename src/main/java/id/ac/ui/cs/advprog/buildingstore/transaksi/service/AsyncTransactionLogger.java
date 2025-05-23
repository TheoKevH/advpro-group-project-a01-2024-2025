package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncTransactionLogger {

    // Async programming untuk log setiap perubahan status
    @Async
    public void logTransactionStatus(Transaction trx) {
        log.info("[LOG] Transaksi ID={} memiliki Status={}", trx.getId(), trx.getStatus());
    }
}
