package uk.ac.ebi.ddi.service.db.repo.enrichment;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.ac.ebi.ddi.service.db.model.enrichment.DatasetEnrichmentInfo;

/**
 * The Access Repository it give information about the access to any resource in the database and the system.
 *
 * @author Mingze
 */

public interface IEnrichmentInfoRepo extends MongoRepository<DatasetEnrichmentInfo,ObjectId>{

    @Query("{'$and':[{'accession': ?0},{'database': ?1}, {'status': ?2}]}")
    public DatasetEnrichmentInfo findByAccessionDatabaseStatusQuery(String accession, String database, String status);

    @Query("{'$and':[{'accession': ?0},{'database': ?1}]}")
    public DatasetEnrichmentInfo findByAccessionDatabaseQuery(String accession, String database);

}
