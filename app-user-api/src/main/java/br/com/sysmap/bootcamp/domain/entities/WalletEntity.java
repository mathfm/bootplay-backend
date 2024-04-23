package br.com.sysmap.bootcamp.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallet")
public class WalletEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @Column(name = "balance")
  private BigDecimal balance;

  @Column(name = "points")
  private Long points;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;

  @OneToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

}
