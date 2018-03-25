package ru.allrecipes.merchant.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Invoice extends DomainId {

  public static enum State {
    NEW, PAID, LOCKED
  }

  @ManyToOne
  private Supplier supplier;

  @ManyToOne
  private Customer customer;

  @Column(updatable = false)
  private Integer invoiceId;

  private String caption;

  private String description;

  private Double amount;

  private OffsetDateTime createdDate;

  private OffsetDateTime paidDate;

  private State state;

  @PrePersist
  private void prePersit() {
    invoiceId = UUID.randomUUID().hashCode();
  }

}
