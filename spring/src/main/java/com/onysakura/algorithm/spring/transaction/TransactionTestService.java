package com.onysakura.algorithm.spring.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionTestService {

    private final Logger log = LoggerFactory.getLogger(TransactionTestService.class);

    @Autowired
    private TransactionTestRepository transactionTestRepository;
    @Autowired
    private TransactionTestServiceSub transactionTestServiceSub;

    @Transactional(propagation = Propagation.REQUIRED)
    public void required_required(long testNo) {
        transactionTestRepository.save(new TransactionTestModel(testNo, "required_required"));
        transactionTestServiceSub.requiredNoException(testNo);
        try {
            transactionTestServiceSub.requiredRuntimeException(testNo);
        } catch (Exception ignored) {
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void required_requiresNew(long testNo) {
        transactionTestRepository.save(new TransactionTestModel(testNo, "required_requiresNew"));
        transactionTestServiceSub.requiresNewNoException(testNo);
        try {
            transactionTestServiceSub.requiresNewRuntimeException(testNo);
        } catch (Exception ignored) {
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void supports_required(long testNo) {
        transactionTestRepository.save(new TransactionTestModel(testNo, "supports_required"));
        transactionTestServiceSub.requiredNoException(testNo);
        try {
            transactionTestServiceSub.requiredRuntimeException(testNo);
        } catch (Exception ignored) {
        }
    }
}
