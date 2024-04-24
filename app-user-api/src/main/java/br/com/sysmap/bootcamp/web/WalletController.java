package br.com.sysmap.bootcamp.web;


import br.com.sysmap.bootcamp.domain.entities.WalletEntity;
import br.com.sysmap.bootcamp.dto.WalletDto;
import br.com.sysmap.bootcamp.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/wallet")
@Table(name = "Wallet")
public class WalletController {
    private final WalletService walletService;

    @Operation(summary = "Realiza um debito na carteira")
    @PostMapping("/credit/{value}")
    public void createWalletEntity(@PathVariable("value") BigDecimal value, @RequestBody WalletDto walletDto) {
        walletDto.setValue(value);
        this.walletService.debit(walletDto);
    }

    @Operation(summary = "Retorna a carteira do usuario")
    @GetMapping("/")
    public WalletEntity getUserWallet() {
        return this.walletService.getUserWallet();
    }

}
