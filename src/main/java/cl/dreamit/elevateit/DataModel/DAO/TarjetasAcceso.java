package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.ResumenTarjetaControlador;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TarjetasAcceso {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(ResumenTarjetaControlador rtc){

    }

    public static void save(ResumenTarjetaControlador[] rtc){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<ResumenTarjetaControlador> rtcList = Arrays.asList(rtc);
        entityManager.getTransaction().begin();
        for (Iterator<ResumenTarjetaControlador> it = rtcList.iterator(); it.hasNext();) {
            ResumenTarjetaControlador enquiry = it.next();
            entityManager.merge(enquiry);
            entityManager.flush();
            entityManager.clear();
        }
        entityManager.getTransaction().commit();
    }


    public static ResumenTarjetaControlador getTarjeta(int cardType, long card_code_1, long card_code_2){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT * FROM resumenTarjetaControlador WHERE card_type = :cardType AND card_code_1 = :card_code_1 AND card_code_2 = :card_code_2 ORDER BY id DESC LIMIT 1"
        )
        .setParameter("cardType", cardType)
        .setParameter("card_code_1", card_code_1)
        .setParameter("card_code_2", card_code_2);
        return (ResumenTarjetaControlador) query.getSingleResult();
    }

    public static void eliminar(ResumenTarjetaControlador rtc){

    }
}