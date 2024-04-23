package br.com.sysmap.bootcamp.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "album")
public class AlbumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "id_spotify", nullable = false, length = 100)
    private String idSpotify;

    @Column(name = "image_url", nullable = false, length = 150)
    private String imageUrl;

    @Column(name = "artist_name", nullable = false, length = 150)
    private String artistName;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity users;

}