package uk.ac.ebi.ddi.service.db.service.dataset;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;
import uk.ac.ebi.ddi.service.db.repo.dataset.IDatasetRepo;

import java.util.List;


/**
 * The DatasetAccess reader that implements all the methods to retrieve a dataset, remove it. or find them.
 *
 * @author Yasset Perez-Riverol
 */
@Component
public class DatasetService implements IDatasetService {

    @Autowired
    private IDatasetRepo datasetAccessRepo;

    @Override
    public Dataset save(Dataset dataset) {
        return datasetAccessRepo.save(dataset);
    }

    @Override
    public Dataset read(ObjectId id) {
        return datasetAccessRepo.findOne(id);
    }

    @Override
    public Page<Dataset> readAll(int pageStart, int size) {
        return datasetAccessRepo.findAll(new PageRequest(pageStart, size));
    }

    @Override
    public Dataset update(ObjectId id, Dataset dataset) {

        Dataset existingDataset = datasetAccessRepo.findOne(id);

        if(existingDataset != null){
            dataset.setId(id);
            return datasetAccessRepo.save(dataset);
        }
        return null;
    }

    @Override
    public Dataset delete(ObjectId id) {
        datasetAccessRepo.delete(id);
        return datasetAccessRepo.findOne(id);
    }


    @Override
    public Dataset read(String acc, String database) {
        return datasetAccessRepo.findByAccessionDatabaseQuery(acc, database);
    }

    @Override
    public List<Dataset> readDatasetHashCode(String database) {
        return datasetAccessRepo.findByDatabase(database);
    }

    @Override
    public Dataset updateCategory(Dataset dataset) {

        Dataset existingDataset = datasetAccessRepo.findOne(dataset.getId());
        existingDataset.setAccession(dataset.getCurrentStatus());
        return datasetAccessRepo.save(existingDataset);
    }
}
