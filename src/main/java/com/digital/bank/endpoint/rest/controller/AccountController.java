package com.digital.bank.endpoint.rest.controller;

import com.digital.bank.component.AccountComponent;
import com.digital.bank.component.AccountStatementComponent;
import com.digital.bank.component.TransactionComponent;
import com.digital.bank.component.dashboard.AccountTotalIncomeExpenseComponent;
import com.digital.bank.component.dashboard.AccountTransactionByCategoryComponent;
import com.digital.bank.endpoint.rest.mapper.AccountMapper;
import com.digital.bank.model.Account;
import com.digital.bank.service.AccountService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
  private final AccountService service;
  private final AccountMapper mapper;

  public AccountController(AccountService service, AccountMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping("")
  public List<AccountComponent> getAllAccounts() {

    return this.service.getAllAccounts().stream()
        .map(this.mapper::toComponent)
        .collect(Collectors.toList());
  }

  @PutMapping("")
  public List<AccountComponent> createOrUpdateAccount(@RequestBody List<Account> toSave) {
    return this.service.createOrUpdateAccounts(toSave).stream()
        .map(this.mapper::toComponent)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public AccountComponent getAccountById(@PathVariable String id) {
    return this.mapper.toComponent(this.service.getAccountById(id));
  }

  @DeleteMapping("/{id}")
  public AccountComponent deleteAccountById(@PathVariable String id) {
    return this.mapper.toComponent(this.service.deleteAccountById(id));
  }

  @GetMapping("/{id}/transaction")
  public List<TransactionComponent> getAllTransactionsOfAccount(@PathVariable String id) {
    return this.service.getAllTransactionsOfAccount(id);
  }

  @GetMapping("/{id}/transaction/by-category")
  public List<AccountTransactionByCategoryComponent> getTransactionsByCategoryAndAccountId(
      @PathVariable String id, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
      throws SQLException {
    return this.service.getTransactionsByCategoryAndAccountId(id, startDate, endDate);
  }

  @GetMapping("/{id}/transaction/income-expense")
  public List<AccountTotalIncomeExpenseComponent> getIncomeExpenseTotalsByAccount(
      @PathVariable String id,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate,
      @RequestParam(required = false, defaultValue = "false") boolean groupByDay)
      throws SQLException {
    return this.service.getIncomeExpenseTotalsByAccount(id, startDate, endDate, groupByDay);
  }

  @GetMapping("/{id}/statement")
  public List<AccountStatementComponent> generateAccountStatement(
      @PathVariable String id, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
    return this.service.generateAccountStatement(id, startDate, endDate);
  }
}
