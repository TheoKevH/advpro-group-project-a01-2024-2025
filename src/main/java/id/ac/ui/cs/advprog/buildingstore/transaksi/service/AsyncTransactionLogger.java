package id.ac.ui.cs.advprog.buildingstore.transaksi.service;

import id.ac.ui.cs.advprog.buildingstore.transaksi.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncTransactionLogger {

    @Async
    public void logTransactionCompletion(Transaction trx) {
        log.info("Transaksi selesai (async log): ID={} Status={}", trx.getId(), trx.getStatus());
    }
}
