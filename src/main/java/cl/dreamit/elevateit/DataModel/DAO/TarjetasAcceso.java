package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.ResumenTarjetaControlador;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public enum TarjetasAcceso {
    INSTANCE;

    public void save(ResumenTarjetaControlador rtc){

    }
    public void save(ResumenTarjetaControlador[] rtc){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        List<ResumenTarjetaControlador> rtcList = Arrays.asList(rtc);
        try{
            entityManager.getTransaction().begin();
            for (Iterator<ResumenTarjetaControlador> it = rtcList.iterator(); it.hasNext();) {
                ResumenTarjetaControlador enquiry = it.next();
                entityManager.merge(enquiry);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex){
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public ResumenTarjetaControlador getTarjeta(int cardType, long card_code_1, long card_code_2){
        EntityManager entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT r FROM ResumenTarjetaControlador r WHERE card_type = :cardType AND card_code_1 = :card_code_1 AND card_code_2 = :card_code_2 ORDER BY id DESC"
        )
        .setMaxResults(1)
        .setParameter("cardType", cardType)
        .setParameter("card_code_1", card_code_1)
        .setParameter("card_code_2", card_code_2);
        ResumenTarjetaControlador outputResult;
        try {
            outputResult = (ResumenTarjetaControlador) query.getSingleResult();
        } catch(NoResultException ex) {
            outputResult = null;
        }
        entityManager.close();
        return outputResult;
    }

    public void eliminar(ResumenTarjetaControlador rtc){

    }
}
