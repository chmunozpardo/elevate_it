package cl.dreamit.elevateit.DataModel.DAO;

import cl.dreamit.elevateit.DataModel.Entities.GK2.ParametroPuntoAcceso;
import cl.dreamit.elevateit.Utils.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.Collection;

public class ParametrosPuntosAccesos {

    @PersistenceContext
    private static EntityManager entityManager;

    public static void save(Collection<ParametroPuntoAcceso> parametrosPuntosAccesos){

    }

    public static ParametroPuntoAcceso getParametroPuntoAcceso(int idPuntoAcceso, String nombreParametro){
        entityManager = PersistenceManager.INSTANCE.getEntityManager();
        Query query = entityManager.createQuery(
            "SELECT * FROM parametroPuntoAcceso WHERE id_punto_acceso = :idPuntoAcceso AND parametro LIKE :nombreParametro"
        )
        .setParameter("idPuntoAcceso", idPuntoAcceso)
        .setParameter("nombreParametro", nombreParametro);
        return (ParametroPuntoAcceso) query.getSingleResult();
    }
}
