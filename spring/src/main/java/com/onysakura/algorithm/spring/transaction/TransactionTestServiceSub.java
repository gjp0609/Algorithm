package com.onysakura.algorithm.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionTestServiceSub {

    @Autowired
    private TransactionTestRepository transactionTestRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredNoException(long testNo) {
        transactionTestRepository.save(new TransactionTestModel(testNo, "requiredNoException"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredRuntimeException(long testNo) {
        transactionTestRepository.save(new TransactionTestModel(testNo, "requiredRuntimeException"));
        throw new RuntimeException();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewNoException(long testNo) {
        transactionTestRepository.save(new TransactionTestModel(testNo, "requiresNewNoException"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewRuntimeException(long testNo) {
        transactionTestRepository.save(new TransactionTestModel(testNo, "requiresNewRuntimeException"));
        throw new RuntimeException();
    }

}
