package com.aprendendotddspring.aprendendo.service.impl;

import com.aprendendotddspring.aprendendo.api.dto.LoanFilterDTO;
import com.aprendendotddspring.aprendendo.api.exceptions.BusinessException;
import com.aprendendotddspring.aprendendo.entity.Book;
import com.aprendendotddspring.aprendendo.entity.Loan;
import com.aprendendotddspring.aprendendo.model.repository.LoanRepostory;
import com.aprendendotddspring.aprendendo.service.LoanService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepostory repository;

    public LoanServiceImpl(LoanRepostory repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {

        if(repository.existsByBookAndNotReturned(loan.getBook())){
            throw new BusinessException("Book already loaned");
        }
        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {

        return repository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable) {
        return repository.findByBookIsbnOrCustumer(filterDTO.getIsbn(), filterDTO.getCustomer(), pageable);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return repository.findByBook(book, pageable);
    }

    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loansDays=4;
        LocalDate threDaysAgo = LocalDate.now().minusDays(loansDays);
        return repository.findByLoansDatesLessThanAndNotReturned(threDaysAgo);
    }


}
