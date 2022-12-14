package com.example.atlantis.service;

import com.example.atlantis.model.*;
import com.example.atlantis.repository.ComentarioRepository;
import com.example.atlantis.repository.HabitacionesRepository;
import com.example.atlantis.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.github.javafaker.Faker;



@Service
public class HotelService {
    private static Faker faker = new Faker();
    private Rol rol;
    private TipoHotel tipoHotel;


    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HabitacionesRepository habitacionesRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    ComentarioService comentarioService;
    @Autowired
    ComentarioRepository comentarioRepository;

    public List<Hotel> getAll(){
        return hotelRepository.findAll();
    }

    public Hotel getById(int id){
        return hotelRepository.findById(id).orElse(null);
    }


    //Convertir el Hotel de un modelo de obtención de datos  al modelo real para meter en bbdd
    public Hotel convertirAHotelApi(GraphqlInput.RegisHotFechInput hotel){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

       LocalDate fechap =  LocalDate.parse(hotel.getFecha_apertura(), formatter);
       LocalDate fechap1 =  LocalDate.parse(hotel.getFecha_cierre(), formatter);

        //Selección de ROL Hotel para el nuevo Hotel
        hotel.getEmail().setRol(GraphqlInput.RolInput.HOTEL);
        Login login = new Login();
        Hotel hotel1 = new Hotel();
        hotel1.setEmail(login);

        //Introducción  de datos en el modelo real para insertar en bbdd
        hotel1.setNombre(hotel.getNombre());
        hotel1.setPais(hotel.getPais());
        hotel1.setLocalidad(hotel.getLocalidad());
        hotel1.setDireccion(hotel.getDireccion());
        hotel1.setFecha_apertura(fechap);
        hotel1.setFecha_cierre(fechap1);
        hotel1.setNum_estrellas(hotel.getNum_estrellas());
        hotel1.setTelefono(hotel.getTelefono());
        hotel1.setTipo_hotel(hotel.getTipo_hotel());
        hotel1.setUrl_icono(hotel.getUrl_icono());
        hotel1.setUrl_imagen_general(hotel.getUrl_imagen_general());
        hotel1.setUrl_imagen_general(hotel.getUrl_imagen_general());
        hotel1.getEmail().setEmail(hotel.getEmail().getEmail());
        hotel1.getEmail().setRol(hotel1.getEmail().getRol());
        hotel1.setLatitud(hotel.getLatitud());
        hotel1.setLongitud(hotel.getLongitud());
        hotel1.getEmail().setPassword(bCryptPasswordEncoder.encode(hotel.getEmail().getPassword()));
        hotel1.getEmail().setRol(Rol.HOTEL);
        hotel1.setId(0);

        return hotel1;
    }


    public Hotel convertirAHotel(RegisHotFech hotel){

        //Selección de ROL Hotel para el nuevo Hotel
        hotel.getEmail().setRol(Rol.HOTEL);

        Hotel hotel1 = new Hotel();

        //Introducción  de datos en el modelo real para insertar en bbdd
        hotel1.setNombre(hotel.getNombre());
        hotel1.setPais(hotel.getPais());
        hotel1.setLocalidad(hotel.getLocalidad());
        hotel1.setDireccion(hotel.getDireccion());
        hotel1.setFecha_apertura(LocalDate.parse(hotel.getFecha_apertura()));
        hotel1.setFecha_cierre(LocalDate.parse(hotel.getFecha_cierre()));
        hotel1.setNum_estrellas(hotel.getNum_estrellas());
        hotel1.setTelefono(hotel.getTelefono());
        hotel1.setTipo_hotel(hotel.getTipo_hotel());
        hotel1.setUrl_icono(hotel.getUrl_icono());
        hotel1.setUrl_imagen_general(hotel.getUrl_imagen_general());
        hotel1.setEmail(hotel.getEmail());
        hotel1.setLatitud(hotel.getLatitud());
        hotel1.setLongitud(hotel.getLongitud());
        hotel1.getEmail().setPassword(bCryptPasswordEncoder.encode(hotel.getEmail().getPassword()));
        hotel1.setId(hotel.getId());

        return hotel1;
    }


    //Función para verificar si el Hotel es Apartamento o no, devuelve un boolean según sea
    public boolean siEsApartaHotelApi(GraphqlInput.RegisHotFechInput hotel){

        boolean i = false;

        if(hotel.getTipo_hotel().equals(TipoHotel.APARTAMENTO)){
            i = false;
        }else{
            i = true;
        }

        return i;
    }


    public boolean siEsApartaHotel(RegisHotFech hotel){

        boolean i = false;

        if(hotel.getTipo_hotel().equals(TipoHotel.APARTAMENTO)){
            i = false;
        }else{
            i = true;
        }

        return i;
    }


    public Hotel guardarHotel(Hotel hotel){
        return hotelRepository.save(hotel);
    }

    public Integer conseguirId(String correo){

        List<Hotel> hoteles = hotelRepository.findAll();
        Integer id = 0;

        for (Hotel x: hoteles){
            if (x.getEmail().getEmail().equals(correo)){
                id = x.getId();
            }
        }

        return id;
    }


    public void borrarHotel(Hotel hotel){

       List<Hotel> todos = hotelRepository.findAll();

        for(int i = 0; i < todos.size(); i++ ){
            if(todos.get(i).getEmail().equals(hotel.getEmail())){

                hotelRepository.delete(hotel);
            }
        }
    }

    public Hotel copiartodohotel(Hotel hotel){

        List<Hotel> todos = hotelRepository.findAll();
        Hotel hotel1 = new Hotel();

        for(int i = 0; i < todos.size(); i++ ){
            if(todos.get(i).getEmail().getEmail().equals(hotel.getEmail().getEmail())){
                hotel1 = todos.get(i);
            }
        }

        return hotel1;
    }



    public void editarHotel(Hotel hotel){

        hotelRepository.editarHotel(hotel.getNombre(), hotel.getPais(), hotel.getLocalidad(),
        hotel.getDireccion(), hotel.getFecha_apertura(), hotel.getFecha_cierre(),
                hotel.getNum_estrellas(), hotel.getTipo_hotel(), hotel.getTelefono(), hotel.getUrl_icono(),
                hotel.getUrl_imagen_general(), hotel.getEmail().getEmail());

    }

    public Map<Hotel, Integer> filtrarmejores (List<Hotel> hoteles){

        Map<Hotel, Integer> mapa = new HashMap<>();

        //Busqueda hoteles para obtener puntuación media
        for (Hotel x: hoteles){
            Integer id = x.getId();
            Integer media = comentarioService.mediaPuntuacion(id);

            if (media.equals(0)){
            }
            else{
                mapa.put(x, media);
            }
        }

        //Ver si el mapa es menor o mayor que 3
        if (mapa.size()<3){
            return mapa;
        }
        else{

            //Ordenamos la lista de hoteles
            List<Hotel> topPuntacion = mapa.keySet().stream().sorted(Comparator.comparing(mapa::get)).collect(Collectors.toList());

            System.out.println(topPuntacion);

            // Sublista con el top 3
            List<Hotel> trePrimeros = topPuntacion.subList(topPuntacion.size()-3, topPuntacion.size());
            topPuntacion.forEach(x-> System.out.println(x.getNombre()));

            //Mapa nuevo vacio
            Map<Hotel, Integer> mapa2 = new HashMap<>();

            //Mapa vacio, metemos los tres hoteles como clave y el valor que tenian
            // los hoteles en el mapa 1
            trePrimeros.forEach(h-> mapa2.put(h, mapa.get(h)));

            return mapa2;
        }


    }

    public Map<Hotel, Integer> filtrarHotel (List<Hotel> hoteles){

        Map<Hotel, Integer> mapa = new HashMap<>();

        //Busqueda de hoteles con su media para introducirlos en el mapa
        for (Hotel x: hoteles){
            Hotel nuevo = x;
            Integer media = comentarioService.mediaPuntuacion(x.getId());
            mapa.put(nuevo, media);
        }

        //Ordenarlo según su puntuación
        mapa.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

    return mapa;
    }

    public Hotel copiartodohotel(String hotel){

        List<Hotel> todos = hotelRepository.findAll();
        Hotel hotel1 = new Hotel();

        for(int i = 0; i < todos.size(); i++ ){
            if(todos.get(i).getEmail().getEmail().equals(hotel)){
                hotel1 = todos.get(i);
            }
        }

        return hotel1;
    }

    public Hotel crearHotel(){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Login login = new Login();
        Hotel hotel = new Hotel();

        //Seteo login
        login.setEmail(faker.internet().emailAddress());
        login.setPassword(passwordEncoder.encode("1234"));
        login.setRol(rol.HOTEL);

        //Seteo hotel
        hotel.setEmail(login);
        hotel.setTipo_hotel(Arrays.stream(TipoHotel.values()).collect(Collectors.toList()).get(faker.number().numberBetween(0,4)));
        hotel.setDireccion(faker.address().fullAddress());
        hotel.setNombre("Hotel "+faker.starTrek().character());
        hotel.setPais(faker.country().name());
        hotel.setLocalidad(faker.country().capital());
        hotel.setLatitud(Double.valueOf(faker.address().latitude().replace(",",".")));
        hotel.setLongitud(Double.valueOf(faker.address().longitude().replace(",",".")));
        hotel.setTelefono(faker.phoneNumber().subscriberNumber(9));
        hotel.setNum_estrellas(faker.number().numberBetween(0,6));
        hotel.setFecha_apertura(fechaAzar2022());
        hotel.setFecha_cierre(fechaAzar2022());

        return hotel;
    }

    private LocalDate fechaAzar2022(){

        LocalDate fecha ;
        int anyo = 2022;
        int mes = faker.number().numberBetween(1,13);
        int dia = 1;
        if (mes ==2) dia = faker.number().numberBetween(1,28);
        else if (mes ==1||mes==3||mes==5||mes==7||mes==8||mes==10||mes==12) dia = faker.number().numberBetween(1,32);
        else if (mes==4||mes==6||mes==9||mes==11) dia = faker.number().numberBetween(1,31);
        fecha = LocalDate.of(anyo,mes,dia);

        return fecha;
    }

    public List<TipoHab> todoTipo (){

        List<TipoHab> tipo = new ArrayList<>();
        tipo.add(TipoHab.SIMPLE);
        tipo.add(TipoHab.DOBLE);
        tipo.add(TipoHab.TRIPLE);
        tipo.add(TipoHab.SUITE);

        return tipo;
    }

    public List<TipoHab> checkTipo (List<Habitaciones> lista){

        List<TipoHab> crear = todoTipo();

        for (Habitaciones x: lista){
            if ( x.getTipo_hab() == TipoHab.SIMPLE){
                crear.remove(x.getTipo_hab());
            }
            if ( x.getTipo_hab() == TipoHab.DOBLE){
                crear.remove(x.getTipo_hab());
            }
            if ( x.getTipo_hab() == TipoHab.TRIPLE){
                crear.remove(x.getTipo_hab());
            }
            if ( x.getTipo_hab() == TipoHab.SUITE){
                crear.remove(x.getTipo_hab());
            }
        }

        return crear;
    }

    public List<Habitaciones> pillarHabitaciones (Integer id){
        List<Habitaciones> lista = habitacionesRepository.findAll().stream().filter(x-> x.getId_hotel().getId().equals(id)).collect(Collectors.toList());
        return lista;
    }


}