package uk.ac.ebi.ddi.service.db.service.enrichment;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.service.db.exception.DBWriteException;
import uk.ac.ebi.ddi.service.db.model.enrichment.EnrichedDataset;
import uk.ac.ebi.ddi.service.db.repo.enrichment.IEnrichmentRepo;

import java.util.ArrayList;

/**
 * Created by mingze on 30/07/15.
 */

@Component
public class EnrichmentService implements IEnrichmentService {


    @Autowired
    private IEnrichmentRepo accessRepo;

    @Override
    public EnrichedDataset insert(EnrichedDataset enrichedDataset) {

        if (accessRepo.findByRepoIdQuery(enrichedDataset.getDatasetRepoId()) != null) {
            return enrichedDataset;
        }

        EnrichedDataset insertedDataset = accessRepo.insert(enrichedDataset);
        if ((insertedDataset.getId() == null))
            throw new DBWriteException("Inserting fail, no _id assigned");
        return insertedDataset;
    }


    @Override
    public EnrichedDataset read(ObjectId id) {
        return accessRepo.findOne(id);
    }

    @Override
    public Page<EnrichedDataset> readAll(int pageStart, int size) {
        return accessRepo.findAll(new PageRequest(pageStart, size));
    }

    @Override
    public EnrichedDataset update(EnrichedDataset enrichedDataset) {
        return accessRepo.save(enrichedDataset);
    }

    @Override
    public EnrichedDataset delete(ObjectId id) {
        accessRepo.delete(id);
        return accessRepo.findOne(id);
    }

    @Override
    public EnrichedDataset readByRepoId(String datasetRepoId) {
        if ((datasetRepoId == null))
            throw new DBWriteException(" The reference to the original resource should contain a string");

        EnrichedDataset enrichedDataset = accessRepo.findByRepoIdQuery(datasetRepoId);
            return enrichedDataset;

    }

    @Override
    public boolean isDatasetExist(String datasetRepoId) {
        EnrichedDataset dataset = accessRepo.findByRepoIdQuery(datasetRepoId);
        if ((dataset != null)) return true;
        else return false;
    }

}
