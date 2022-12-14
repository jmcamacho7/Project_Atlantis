package com.example.atlantis.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "hotel")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Integer id;

    @JoinColumn(name = "nombre")
    private String nombre;

    @JoinColumn(name = "pais")
    private String pais;

    @JoinColumn(name = "localidad")
    private String localidad;

    @JoinColumn(name = "direccion")
    private String direccion;

    @JoinColumn(name = "fecha_apertura")
    private LocalDate fecha_apertura;

    @JoinColumn(name = "fecha_cierre")
    private LocalDate fecha_cierre;

    @JoinColumn(name = "num_estrellas")
    private int num_estrellas;

    @JoinColumn(name = "tipo_hotel")
    private TipoHotel tipo_hotel;

    @JoinColumn(name = "telefono")
    private String telefono;

    @JoinColumn(name = "email")
    @ManyToOne(cascade = CascadeType.ALL)
    private Login email;

    @JoinColumn(name = "url_icono")
    private String url_icono;

    @JoinColumn(name = "url_imagen_general")
    private String url_imagen_general;

    @JoinColumn(name = "latitud")
    private Double latitud ;

    @JoinColumn(name = "longitud")
    private Double longitud ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Comentario> comentarios;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id_hotel")
    private List<Habitaciones> habitaciones;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id_hotel")
    private List<Regimen> regimen;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id_hotel")
    private List<Reserva> reserva;

}
