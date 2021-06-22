package nz.co.delacour.firefall.core.load;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.common.collect.Lists;
import nz.co.delacour.firefall.core.HasId;
import nz.co.delacour.firefall.core.registrar.LifecycleMethod;

import java.util.List;

import static nz.co.delacour.firefall.core.FirefallService.getMetadata;


public class ListResult<T extends HasId<T>> {

    private List<T> items;

    private String cursor;

    public ListResult(Class<T> entityClass, List<QueryDocumentSnapshot> documents) {

        var numberOfDocuments = documents.size();
        if (numberOfDocuments > 0) {
            var lastDocument = documents.get(numberOfDocuments - 1);
            if (lastDocument != null) {
                this.cursor = lastDocument.getId();
            }
        }

        //Get last item from list set as cursor
        List<T> entities = Lists.newArrayList();
        for (QueryDocumentSnapshot document : documents) {
            T t = document.toObject(entityClass);
            t.setId(document.getId());
            entities.add(t);
        }

        var onLoadMethods = getOnLoadMethods(entityClass);
        for (T entity : entities) {
            for (LifecycleMethod onLoadMethod : onLoadMethods) {
                onLoadMethod.execute(entity);
            }
        }

        this.items = entities;
    }

    private List<LifecycleMethod> getOnLoadMethods(Class<T> entityClass) {
        var metadata = getMetadata(entityClass);
        if (metadata == null) {
            return Lists.newArrayList();
        }
        return metadata.getOnLoadMethods();
    }

    public List<T> items() {
        return items;
    }

    public String cursor() {
        return cursor;
    }
}
