package br.com.sysmap.bootcamp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class AlbumDto implements Serializable {

  private String name;
  private String idSpotify;
  private String artistName;
  private String imageUrl;
  private BigDecimal value;
}
