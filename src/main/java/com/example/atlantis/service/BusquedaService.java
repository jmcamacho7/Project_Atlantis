package com.example.atlantis.service;

import com.example.atlantis.model.Busqueda;
import com.example.atlantis.model.GraphqlInput;
import com.example.atlantis.model.Habitaciones;
import com.example.atlantis.model.Hotel;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BusquedaService {
    public List<Hotel> AccionBuscar(Busqueda busqueda, List<Hotel> listaHoteles){

        String strBusqueda;
        List<Hotel> coincidencias = new ArrayList<>();
        strBusqueda = busqueda.getHotelBuscar().toLowerCase();

        for( Hotel hotel:listaHoteles){             //buscar en la lista de hoteles
            if(estaAbierto(hotel,busqueda)){        //si el hotel esta abierto
                int libres = 0;

                for (Habitaciones hab :hotel.getHabitaciones()){
                    if (hab.getHab_ocupadas()< hab.getNum_hab()){
                        libres += ((hab.getNum_hab()-hab.getHab_ocupadas()) * hab.getMax_cliente());
                    }
                }
                if (libres>= busqueda.getNumHuespedes()){
                    if((hotel.getLocalidad().toLowerCase().contains(strBusqueda))|| //igualamos a minusculas los string
                            (hotel.getNombre().toLowerCase().contains(strBusqueda))){ //y añadimos las coincidencias
                        coincidencias.add(hotel);
                    }
                }
            }
        }

        return coincidencias;
    }


    public List<Hotel> AccionBuscarApi(GraphqlInput.BusquedaInput busqueda, List<Hotel> listaHoteles){

        String strBusqueda;
        List<Hotel> coincidencias = new ArrayList<>();
        strBusqueda = busqueda.getHotelBuscar().toLowerCase();

        for( Hotel hotel:listaHoteles){             //buscar en la lista de hoteles
            if(estaAbiertoApi(hotel,busqueda)){        //si el hotel esta abierto
                int libres = 0;
                for (Habitaciones hab :hotel.getHabitaciones()){
                    if (hab.getHab_ocupadas()< hab.getNum_hab()){
                        libres += ((hab.getNum_hab()-hab.getHab_ocupadas()) * hab.getMax_cliente());
                    }
                }
                if (libres>= busqueda.getNumHuespedes()){
                    if((hotel.getLocalidad().toLowerCase().contains(strBusqueda))|| //igualamos a minusculas los string
                            (hotel.getNombre().toLowerCase().contains(strBusqueda))){ //y añadimos las coincidencias
                        coincidencias.add(hotel);
                    }
                }
            }
        }

        return coincidencias;
    }

    private boolean estaAbierto(Hotel hotel, Busqueda busqueda ){ //el hotel esta abierto si la fecha de busqueda está

        //entre las fechas de apertura del hotel
        if((hotel.getFecha_apertura().isBefore(LocalDate.parse(busqueda.getFechaInicial()))&&
                (hotel.getFecha_cierre().isAfter(LocalDate.parse(busqueda.getFechaFinal()))))){
            return true;
        }
        return false;
    }

    private boolean estaAbiertoApi(Hotel hotel,GraphqlInput.BusquedaInput busqueda ){ //el hotel esta abierto si la fecha de busqueda está

        //entre las fechas de apertura del hotel
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate fechap = LocalDate.parse(busqueda.getFechaInicial(), formatter);
        LocalDate fechap1 = LocalDate.parse(busqueda.getFechaFinal(), formatter);


        if((hotel.getFecha_apertura().isBefore(fechap)&&
                (hotel.getFecha_cierre().isAfter(fechap1)))){

            return true;
        }
        return false;
    }

}
