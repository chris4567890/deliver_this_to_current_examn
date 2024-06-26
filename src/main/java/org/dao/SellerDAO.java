package org.dao;

import org.config.HibernateConfig;
import org.model.Car;
import org.model.Role;
import org.model.Seller;

import java.util.HashSet;
import java.util.Set;
//most of this is also rewritten from one of my group projects
public class SellerDAO extends DAO<Seller,String> {
    private static SellerDAO instance;

    public static SellerDAO getInstance(boolean isTesting){
        if(instance == null){
            instance = new SellerDAO(isTesting);
        }
        return instance;
    }

    public SellerDAO(boolean isTesting){
        super(Seller.class,isTesting);
    }


    @Override
    public Seller getById(String id) {
        System.out.println("I am inside get by id");
        Seller seller;

        try(var em = HibernateConfig.getEntityManagerFactoryConfig().createEntityManager()){
            //em.getTransaction().begin();
            seller = em.find(Seller.class,id);
            System.out.println("here is your found seller: "+seller);

        }
        return seller;
    }


    public void addCarToSeller(String seller_id, int car_id){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            Car foundCar = em.find(Car.class,car_id);
            Seller foundSeller = em.find(Seller.class,seller_id);
            Set<Car> foundcars = foundSeller.getCars();
            if(foundCar != null && foundSeller != null){
                em.getTransaction().begin();
                foundcars.add(foundCar);
                foundSeller.setCars(foundcars);
                em.merge(foundSeller);
                em.getTransaction().commit();
            }
        }
    }

    public static void main(String[] args) {
        SellerDAO sellerDAO = SellerDAO.getInstance(false);
        Car car = new Car(3,"49","44","444","4847",199,399);
        Set<Car> cars = new HashSet<>();
        cars.add(car);
        Seller seller = new Seller("thomas@thomas.com","thomas","thomas","1234567890",4854,"Lyngby",cars,Set.of(Role.admin,Role.seller));
        sellerDAO.addCarToSeller(seller.getEmail(),car.getId());
    }
    public Set getCarsBySeller(String id){
        try(var em = emf.createEntityManager()){
            em.getTransaction().begin();
            Seller foundSeller = em.find(Seller.class,id);
            if(foundSeller != null){
                return foundSeller.getCars();
            }else{
                return null;
            }
        }
    }

}
