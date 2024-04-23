package br.com.sysmap.bootcamp.domain.listeners;

import br.com.sysmap.bootcamp.dto.WalletDto;
import br.com.sysmap.bootcamp.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@RabbitListener(queues = "WalletQueue")
public class WalletListener {

    @Autowired
    private WalletService walletService;

    @RabbitHandler
    public void recieve(WalletDto walletDto) {
        try {
            walletService.debit(walletDto);
            log.info(walletDto.getEmail(), walletDto.getValue());
        } catch (RuntimeException e) {
          log.error("Error processing debit operation: {}", e.getMessage());
        }
    }
}
