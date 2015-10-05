package uk.ac.ebi.ddi.service.db.service.similarity;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.service.db.exception.DBWriteException;
import uk.ac.ebi.ddi.service.db.model.similarity.ExpOutputDataset;
import uk.ac.ebi.ddi.service.db.repo.similarity.IExpOutputDatasetRepo;

import java.util.List;

/**
 * Created by mingze on 30/07/15.
 */

@Component
public class ExpOutputDatasetService implements IExpOutputDatasetService {


    @Autowired
    private IExpOutputDatasetRepo accessRepo;

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public ExpOutputDataset insert(ExpOutputDataset expOutputDataset) {

        if (accessRepo.findByAccessionQuery(expOutputDataset.getAccession()) != null) {
            return expOutputDataset;
        }

        ExpOutputDataset insertedDataset = accessRepo.insert(expOutputDataset);
        if ((insertedDataset.getId() == null))
            throw new DBWriteException("Inserting fail, no _id assigned");
        return insertedDataset;
    }


    @Override
    public ExpOutputDataset read(ObjectId id) {
        return accessRepo.findOne(id);
    }

    @Override
    public Page<ExpOutputDataset> readAll(int pageStart, int size) {
        System.out.println("pageStart and size:" + pageStart + "," + size);
//        System.exit(1);
        if(size<1){return null;}
        return accessRepo.findAll(new PageRequest(pageStart, size));
    }

    @Override
    public List<ExpOutputDataset> readAllInOneType(String dataType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("expDataType").is(dataType));
        List<ExpOutputDataset> allDatasetInOneType = mongoOperation.find(query, ExpOutputDataset.class);
        return allDatasetInOneType;
    }

    @Override
    public ExpOutputDataset update(ExpOutputDataset expOutputDataset) {
        return accessRepo.save(expOutputDataset);
    }

    @Override
    public ExpOutputDataset delete(ObjectId id) {
        accessRepo.delete(id);
        return accessRepo.findOne(id);
    }

    @Override
    public ExpOutputDataset readByaccession(String accession) {
        if ((accession == null))
            throw new DBWriteException(" The accession to the original resource should contain a string");

        ExpOutputDataset expOutputDataset = accessRepo.findByAccessionQuery(accession);
            return expOutputDataset;

    }

    @Override
    public boolean isDatasetExist(String accession) {
        ExpOutputDataset dataset = accessRepo.findByAccessionQuery(accession);
        if ((dataset != null)) return true;
        else return false;
    }

    @Override
    public long getNumberOfDatasets() {
        return (accessRepo.count());
    }

    @Override
    public void deleteAll() {
        accessRepo.deleteAll();
    }
}
